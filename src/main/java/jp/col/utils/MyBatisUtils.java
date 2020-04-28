package jp.col.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtils {

	private static SqlSessionFactory sqlSessionFactory;
	private static SqlSession sqlSession;
 
    public static SqlSession getSqlSession() {
        try {
        	if(sqlSession == null) {
                if (sqlSessionFactory == null) {
                	Properties properties = new Properties();
                	String driver = System.getenv().get("JDBC_DRIVER");
                	String url = System.getenv().get("JDBC_URL");
                	String username = System.getenv().get("JDBC_USERNAME");
                	String password = System.getenv().get("JDBC_PASSWORD");
                	properties.setProperty("jdbc.driver", "org.postgresql.Driver");
                	properties.setProperty("jdbc.url", "jdbc:postgresql://ec2-50-17-178-87.compute-1.amazonaws.com:5432/d5sme73ikbjvoo");
                	properties.setProperty("jdbc.username", "pcivibzuoufwph");
                	properties.setProperty("jdbc.password", "470c7c25763449dfc730575ce494daeabe19cb6123b8baca39ff16a4fe7cdc20");
                	Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
                	SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
                	sqlSessionFactory = builder.build(reader, properties);
                }
        	}
        	
            return sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
