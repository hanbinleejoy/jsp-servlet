package kr.co.acorn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.acorn.dto.MemberDto;
import kr.co.acorn.util.ConnLocator;

public class MemberDao {
	private static MemberDao single;

	private MemberDao() {

	}

	public static MemberDao getInstance() {
		if (single == null) {
			single = new MemberDao();
		}
		return single;
	}

	public boolean isEmail(String email) {
		boolean isSuccess = false;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ConnLocator.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT m_email ");
			sql.append("FROM member ");
			sql.append("WHERE m_email = ? ");

			pstmt = con.prepareStatement(sql.toString());
			int index = 0;
			pstmt.setString(++index, email);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				isSuccess = true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	public MemberDto getMember(MemberDto dto) {
		// 여기서는 매개변수 dto 안에 정보가 담겨있기 때문에
		// 메서드 안에서 새로 dto 객체를 만드는 것이 좋다.
		MemberDto memberDto = new MemberDto();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ConnLocator.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT m_email, m_name, m_phone, DATE_FORMAT(m_regdate, '%Y/%m/%d') ");
			sql.append("FROM member ");
			sql.append("WHERE m_email = ? AND m_pwd = PASSWORD(?) ");

			pstmt = con.prepareStatement(sql.toString());

			int index = 0;
			pstmt.setString(++index, dto.getEmail());
			pstmt.setString(++index, dto.getPassword());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				index = 0;
				String email = rs.getString(++index);
				String name = rs.getString(++index);
				String phone = rs.getString(++index);
				String regdate = rs.getString(++index);
				
				memberDto = new MemberDto(email, name, null, phone, regdate);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return memberDto;
	}

}
