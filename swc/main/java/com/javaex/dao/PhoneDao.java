package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;

@Repository //applicationContext.xml을 넣어준 다음 서로 작용할 수 있게 만든다. 없을때는 같이 없어야 한다. 무조건 넣어서는 안되는 파일.
public class PhoneDao {
	
	//필드
	@Autowired
	private DataSource dataSource;
	
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/*
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";
	*/
	
	//생성자
	//메소드-g/s
	//메소드-일반
	
	//DB접속
	private void getConnection() {
		try {
			conn = dataSource.getConnection();
			
			/*
			// 1. JDBC 드라이버 (Oracle) 로딩                      
			Class.forName(driver);                           
						                                                 
			// 2. Connection 얻어오기                            
			conn = DriverManager.getConnection(url, id, pw); 
			*/
		} /*catch (ClassNotFoundException e) {
		    System.out.println("error: 드라이버 로딩 실패 - " + e);
		} */catch (SQLException e) {
		    System.out.println("error:" + e);
		}
	}
	
	private void close() {   //물리적으로 끊어지지는 않는다.
		// 5. 자원정리
	    try {
	    	if (rs != null) {
	            rs.close();
	        }  
	        if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    }
	}
	
	//리스트 목록 조회
	//select
	public List<PersonVo> getPersonList() {
		List<PersonVo> phoneList = new ArrayList<PersonVo>();
		
		getConnection();
		
		try {

			String query = "";
			query += " select  person_id, ";
			query += "         name,      ";
			query += "         hp,        ";
			query += "         company    ";
			query += " from person        ";
			query += " order by person_id ";
			
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			//결과처리
			while(rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				PersonVo pvo = new PersonVo(personId, name, hp, company);
				phoneList.add(pvo);
			}
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}

		close();
		
		return phoneList;
	}
	
	//insert 저장
	public int personInsert(PersonVo pvo) {
		int count = 0;                                                             
                                                                         
		getConnection();                                                           
		                                                                           
		try {                                                                         
			/*
			insert into person
			values (seq_person_id.nextval, '김경아', '010-6666-6666', '02-6666-6666');
			*/
			
			String query = "";
			query += " insert into person  ";
			query += " values (seq_person_id.nextval, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, pvo.getName());
			pstmt.setString(2, pvo.getHp());
			pstmt.setString(3, pvo.getCompany());

			count = pstmt.executeUpdate();

			// 4. 결과처리 
			System.out.println("[ " + count + " 건 등록되었습니다. ]");
			System.out.println("");
			
		} catch (SQLException e) {                                                 
		    System.out.println("error:" + e);                                      
		}                                                                          
		                                                                                                                                        
		close();                                                                   
		
		return count;
	}
	
	//수정(update)
	public int personUpdate (PersonVo pvo) {
		int count = 0;
		getConnection();
		
		try {

			String query = "";
			query += " update person       ";
			query += " set name = ? ,      ";
			query += "     hp = ? ,        ";
			query += "     company = ?     ";
			query += " where person_id = ? ";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, pvo.getName());
			pstmt.setString(2, pvo.getHp());
			pstmt.setString(3, pvo.getCompany());
			pstmt.setInt(4, pvo.getPersonId());
			
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println("[ " + count + " 건 수정되었습니다. ]");
			System.out.println("");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
		
		//자원정리
		close();
		return count;
	}
	
	//delete
	public int personDelete(int personId) {
		int count = 0;
		
		getConnection();
		
		try {

			String query = "";
			query += " delete from person  ";
			query += " where person_id = ? ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);

			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println("[ " + count + " 건 삭제되었습니다. ]");
			System.out.println("");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
		
		//자원정리
		close();
		return count;
	}
	
	//select 검색(like문 이용)
	public List<PersonVo> personSearch(String str) {
		List<PersonVo> personList = new ArrayList<PersonVo>();                                                
                                                                                                         
		getConnection();                                                                                        
		                                                                                                        
		try {                                                                                                   
			
			String query = "";
			query += " select  person_id,  ";
			query += "         name,       ";
			query += "         hp,         ";
			query += "         company     ";
			query += " from person         ";
			query += " where name like ?   ";
			query += " or hp like ?        ";
			query += " or company like ?   ";
			query += " order by person_id  ";
			
			pstmt = conn.prepareStatement(query);

			
			pstmt.setString(1, "%" + str + "%");
			pstmt.setString(2, "%" + str + "%");
			pstmt.setString(3, "%" + str + "%");
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				PersonVo pvo = new PersonVo(personId, name, hp, company);
				personList.add(pvo);
			}
			
			// 4. 결과 처리                                                                                                   
		    System.out.println("[ 검색어 ' " + str + " ' 이(가) 포함된 리스트 입니다. ]");                                                                                                    
		} catch (SQLException e) {                                                                              
		    System.out.println("error:" + e);                                                                   
		}                                                                                                       
		                                                                                                        
		//자원정리                                                                                                  
		close();                                                                                                
	
		return personList;
	}
	 	//사람 1명 정보 가져오기
	 	public PersonVo getPerson(int personId) { // PersonVo로 불러오기: int x
	 		PersonVo personVo = null;
			getConnection();
			try {
				
				String query = "";
				query += " select  person_id,  ";
				query += "         name,       ";
				query += "         hp,         ";
				query += "         company     ";
				query += " from person         ";
				query += " where person_id = ? ";
			
				pstmt = conn.prepareStatement(query); //쿼리문 1차 완성
				pstmt.setInt(1, personId);            //?세팅 : personId -> 완성되게 넣어준다.
				
				rs = pstmt.executeQuery();            //결과문 완성 -> 결과처리
				
				//결과처리
				while(rs.next()) {
					int personID = rs.getInt("person_id");
					String name = rs.getString("name");
					String hp = rs.getString("hp");
					String company = rs.getString("company");
					
					personVo = new PersonVo(personID, name, hp,company);
				}
				
			}catch (SQLException e) {                                                                              
			    System.out.println("error:" + e);                                                                   
			}    
			close();
			return personVo;
	 }
}