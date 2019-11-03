package com.example.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class JsoupDownloadPicTest {
    /**
     * 准备抓取的目标地址，%E4%BA%92%E8%81%94%E7%BD%91 为utf-8格式的 互联网
     */


    public static void main(String[] args) throws Exception {
       JsoupDownloadPicUtils.downloadPictures("动画");
    }

}

