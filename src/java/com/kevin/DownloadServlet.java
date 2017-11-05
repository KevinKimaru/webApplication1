/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kevin Kimaru Chege
 */
@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String specFile = req.getParameter("file");
        
//        String filePath = "F:\\PROJECTS\\Netbeans projects\\testingServlet\\resources\\" + specFile;
//        File file = new File(filePath);
        
//        String filePath = new File(".").getCanonicalPath();
//        System.out.println(filePath);
//        File file = new File(filePath + "\\resources\\" + specFile);

        String filePath = getServletContext().getRealPath("resources" + File.separator + specFile);
        File file = new File(filePath);
        
        FileInputStream fis = new FileInputStream(file);

        //obtain servlet context
        ServletContext context = getServletContext();

        //gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            //set binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        //modify response
        resp.reset();
        resp.setContentType(mimeType);          
        resp.setContentLengthLong(file.length());

        //forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
        
        resp.setHeader("content-length", String.valueOf(file.length()));
        resp.setHeader(headerKey, headerValue);

        //obtain response's outputstream
        OutputStream out = resp.getOutputStream();

        byte[] buffer = Files.readAllBytes(file.toPath());
        int bytesRead = -1;

        while ((bytesRead = fis.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        fis.close();
        out.close();
    }
}
