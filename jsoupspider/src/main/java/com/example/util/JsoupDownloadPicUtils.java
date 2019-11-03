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

public class JsoupDownloadPicUtils {
    public static void downloadPictures(String keyboard){
//      拼接url
        String url = getUrl(keyboard,1);
//      爬取url文档
        Document document=getDocumentByUrl(url);
//      获取搜索的页数
        Elements elements1 = document.getElementsByTag("span");//搜索结果总条数对应的标签
        String resultNum=elements1.html().replaceAll("[^0-9]","");//将搜索结果总条数提取出来
        int pageNum=Integer.parseInt(resultNum)%17!=0?Integer.parseInt(resultNum)/17+1:Integer.parseInt(resultNum)/17;//将总条数转换成总页数
        System.out.println("共有"+pageNum+"页结果");
        for (int i=1;i<=pageNum;i++){
            System.out.println("正在下载第"+i+"页");
//          拼接对应搜索文本、页数的url
            String url1=getUrl(keyboard,i);
//          下载对应url的图片
            traverseDownload(url1);
            System.out.println("完成下载第"+i+"页");
        }
        System.out.println("下载完成！");
    }
    /**
     * 根据搜索条件和页数进行拼接搜索url
     * @param keyBoard
     * @param pageNum
     * @return
     */
    public static String getUrl(String keyBoard,int pageNum){
        String keyboard=encodeChinese(keyBoard);
        String url = "http://www.netbian.com/e/sch/index.php?page="+(pageNum-1)+"&keyboard="+keyboard;
        return url;
    }

    /**
     * 下载当前url网页中的壁纸
     * @param url
     * @return
     */
    public static boolean traverseDownload(String url){
        Document document = getDocumentByUrl(url);
        //获取指定标签的数据
        Elements elements = document.getElementsByClass("list");
        Elements listEle=elements.get(0).children().get(0).children();
        //获取所有图片链接
        for (int i = 0; i < listEle.size(); i++) {
            System.out.println("正在下载第一张");
            String imgName=listEle.get(i).getElementsByTag("a").text();
            String url1="http://www.netbian.com"+listEle.get(i).getElementsByTag("a").attr("href");
            Document document1=getDocumentByUrl(url1);
            String url2="http://www.netbian.com"+document1.getElementsByClass("pic-down").get(0).getElementsByTag("a").attr("href");
            Document document2=getDocumentByUrl(url2);
            String imgURL=document2.getElementsByTag("td").get(0).getElementsByTag("img").attr("src");
            String downloadPath=System.getProperty("user.dir").concat("\\download");
            downImages(downloadPath,imgURL,imgName);
        }
        return true;
    }

    /**
     * 对中文搜索字符编码
     * @param chineseWord
     * @return
     */
    public static String encodeChinese(String chineseWord){
        try {
            return URLEncoder.encode(chineseWord, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 通过url获得网页文档
     * @param url
     * @return
     */
    public static Document getDocumentByUrl(String url){
        //链接到目标地址
        Connection connect = Jsoup.connect(url);
        //设置useragent,设置超时时间，并以get请求方式请求服务器
        Document document = null;
        try {
            document = connect.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(6000).ignoreContentType(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return document;
    }
    /**
     * 下载图片到指定目录
     *  @param filePath 文件路径
     * @param imgUrl   图片URL
     * @param imgName
     */

    /**
     * 下载图片到本地
     * @param filePath
     * @param imgUrl
     * @param imgName
     */
    public static void downImages(String filePath, String imgUrl, String imgName) {
        // 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());

        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        String suffix=fileName.substring(fileName.lastIndexOf("."),fileName.length());
        File file = new File(filePath + File.separator + imgName+suffix);

        try {
            if (!imgUrl.startsWith("http://")){
                imgUrl="http:"+imgUrl;
            }
            // 获取图片URL
            URL url = new URL(imgUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   /* 作者：qq_璀璨星辉_0
    链接：https://www.imooc.com/article/254681
    来源：慕课网
    本文原创发布于慕课网 ，转载请注明出处，谢谢合作*/
}
