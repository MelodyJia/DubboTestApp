package org.github.service.impl;

import org.github.api.DubboDefaultTestService;
import org.github.api.DubboThriftTestService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class DubboDefaultTestServiceImpl implements DubboDefaultTestService,DubboThriftTestService.Iface {
    @Override
    public int getInt() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Random().nextInt();
    }

    @Override
    public String getString() {


     //调用数据库
        //databaseExecute();
        //调用nosql
        RedisClient rc=new RedisClient();
        rc.ListOperate();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return UUID.randomUUID().toString();
    }

    @Override
    public void setString(String key, String value) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(key+"||||||"+value);
    }

    @Override
    public String getString(String key) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return key;
    }

    public PreparedStatement getPrepareStatement(Connection conn,String sql){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
    //释放资源
    public static void realeaseResource(ResultSet rs,PreparedStatement ps,Connection conn){
        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(null != ps){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void databaseExecute(){
        for (int i = 0; i < 2; i++) {
            Connection conn = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            PreparedStatement ps3 = null;
            ResultSet rs3 = null;
            try {
                conn = C3P0Properties.getInstance().getConnection();
                String sql1 = "insert into testUser(userName,pwd) values(?,?)";
                String sql2 = "delete from testUser where userName=?";
                String sql3 = "select * from testUser";
                ps1 =getPrepareStatement(conn, sql1);
                ps1.setObject(1, "t");
                ps1.setObject(2, "t");
                int count = ps1.executeUpdate();

                if (count > 0) {
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
                } else {
                    System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                }
                ps2 = getPrepareStatement(conn, sql2);
                ps2.setObject(1, "t");
                ps2.executeUpdate();

                ps3 = getPrepareStatement(conn, sql3);
                rs3 = ps3.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                //释放资源
              realeaseResource(null, ps1, conn);
              realeaseResource(null, ps2, conn);
              realeaseResource(rs3, ps3, conn);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
