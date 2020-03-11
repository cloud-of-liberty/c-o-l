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
                	properties.setProperty("jdbc.driver", driver);
                	properties.setProperty("jdbc.url", url);
                	properties.setProperty("jdbc.username", username);
                	properties.setProperty("jdbc.password", password);

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
