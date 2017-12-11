package com.hospital.wechat.servlet;

import com.hospital.wechat.service.GetOpenIdOauth2;
import com.hospital.wechat.service.AccessTokenMgrHXTS;
import com.hospital.wechat.service.AccessTokenMgr;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by QQQ on 17/10/16.
 * for testing usage
 */
@WebServlet(name = "setUsrGroup")
public class setUsrGroup extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AccessTokenMgr mgr = AccessTokenMgrHXTS.getInstance();
        String open_id = GetOpenIdOauth2.getOpenId(request.getParameter("code"), mgr);
        System.out.println("open_id = " + open_id);

        /*
        if (open_id.equals(null)) {
            System.out.println("open id not found");
            response.sendRedirect("http://projecthope.flzhan.com/text398.html");
            //out.println("系统错误，请稍后再试");
        } else {
            UserGroupMgr user_group_mgr = new UserGroupMgr(mgr);

            JSONArray old_group_ids = user_group_mgr.getUserGroups(open_id);
            if (!old_group_ids.isEmpty()) {
                for (int i = 0; i < old_group_ids.size(); i++) {
                    user_group_mgr.deleteUserGroup(old_group_ids.getInteger(i), open_id);
                }
            }

            JSONArray groups = user_group_mgr.getGroups().getJSONArray("tags");

            System.out.println("groups = " + groups.toString());

            Integer tag_id = -1;

            System.out.println("groups.size = " + groups.size());

            if (state.equals("1")) {
                for (int i = 0; i < groups.size(); i++) {
                    System.out.println("1group " + i + " name " + groups.getJSONObject(i).getString("name"));
                    if (groups.getJSONObject(i).getString("name").equals("医护人员")) {
                        tag_id = groups.getJSONObject(i).getInteger("id");
                        System.out.println("tag_id = " + tag_id);
                    }
                }
            } else {
                for (int i = 0; i < groups.size(); i++) {
                    System.out.println("2group " + i + " name " + groups.getJSONObject(i).getString("name"));
                    if (groups.getJSONObject(i).getString("name").equals("非医护人员")) {
                        tag_id = groups.getJSONObject(i).getInteger("id");
                    }
                }
            }

            System.out.println("tag_id = " + tag_id);
            if (tag_id.equals(-1)) {
                System.out.println("tag id not found");
                response.sendRedirect("http://projecthope.flzhan.com/text398.html");
                //out.println(" set tag failed");
            } else {
                if (user_group_mgr.setUserGroup(tag_id.intValue(), open_id)) {
                    System.out.println("set tag succeed");
                    response.sendRedirect("http://projecthope.flzhan.com/text408.html");
                    //out.println(" set tag succeed");
                } else {
                    System.out.println("set tag failed");
                    response.sendRedirect("http://projecthope.flzhan.com/text408.html");
                    //out.println(" set tag failed");
                }
            }
        }
        */

        return;

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
        return;
    }
}
