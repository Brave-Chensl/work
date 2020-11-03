package com.chensl.reptile;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chensl.reptile.domain.Movie;
import com.chensl.reptile.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
class ChenReptileApplicationTests {

	@Test
	public void test01() throws URISyntaxException, IOException {
		//页面请求地址
		//https://movie.douban.com/explore#!type=movie&tag=%E7%83%AD%E9%97%A8&sort=time&page_limit=20&page_start=0
		//数据Ajax实际请求地址
		//https://movie.douban.com/j/search_subjects?type=movie&tag=热门&sort=time&page_limit=20&page_start=0
		String url = "https://movie.douban.com/j/search_subjects";
		//创建请求参数容器
		Map<String, String> parameter = new HashMap<>();
		//创建请求头容器
		Map<String, String> header = new HashMap<>();//创建请求参数容器
		//设置请求参数
		parameter.put("type", "movie");
		parameter.put("tag", "热门");
		parameter.put("sort", "time");
		parameter.put("page_limit", "20");
		//设置请求头
		header.put("Accept", "*/*");
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0");
		header.put("Cookie","bid=9GYWiN77_N0; _pk_id.100001.4cf6=9d2353e1eb3041b0.1604299278.8.1604364055.1604333809.; __utma=30149280.1265812231.1604299279.1604333811.1604364038.8; __utmz=30149280.1604299279.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=223695111.1241323329.1604299279.1604333811.1604364039.8; __utmz=223695111.1604299279.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __yadk_uid=fi8OldaMEy6luDNXYhNO1y8WXmUp05P5; __gads=ID=d633df22179bc014-225767b287c400b4:T=1604299280:S=ALNI_MbYF3ePB7Y0IBAaIlPBRQV6MjzD3g; ll=\"118281\"; _vwo_uuid_v2=D043E56F896003C9C78E0C7566B6FDF35|a5645a53b021ef1b9811d55c131668ec; _pk_ses.100001.4cf6=*; ap_v=0,6.0; __utmb=30149280.0.10.1604364038; __utmc=30149280; __utmb=223695111.0.10.1604364039; __utmc=223695111");


		//设置详细信息的请求参数，请求tou
		//创建请求参数容器
		Map<String, String> parameter2 = new HashMap<>();
		//创建请求头容器
		Map<String, String> header2 = new HashMap<>();//创建请求参数容器
		//设置请求参数
		parameter2.put("from", "gaia");
		parameter2.put("tag", "热门");
		//设置请求头
		header2.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header2.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0");
		header2.put("Cookie","bid=9GYWiN77_N0; _pk_id.100001.4cf6=9d2353e1eb3041b0.1604299278.10.1604371816.1604368416.; __utma=30149280.1265812231.1604299279.1604368416.1604371818.10; __utmz=30149280.1604299279.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=223695111.1241323329.1604299279.1604368416.1604371818.10; __utmz=223695111.1604299279.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __yadk_uid=fi8OldaMEy6luDNXYhNO1y8WXmUp05P5; __gads=ID=d633df22179bc014-225767b287c400b4:T=1604299280:S=ALNI_MbYF3ePB7Y0IBAaIlPBRQV6MjzD3g; ll=\"118281\"; _vwo_uuid_v2=D043E56F896003C9C78E0C7566B6FDF35|a5645a53b021ef1b9811d55c131668ec; __utmc=30149280; __utmc=223695111; _pk_ses.100001.4cf6=*; ap_v=0,6.0; __utmb=30149280.0.10.1604371818; __utmb=223695111.0.10.1604371818");




		//  略过前0条数据，取20条  可以自行更改
		for(int i = 0; i < 20; i+=20){
			parameter.put("page_start", i+"");
			String html = HttpUtils.doGetHtml(url, parameter, header);  //调用请求方法
			JSONObject jsonObject = JSONObject.parseObject(html);
			JSONArray jsonArray = jsonObject.getJSONArray("subjects");//取json数据
			for(int j = 0; j < jsonArray.size(); j++){  //循环遍历每个json对象
				Movie movie = new Movie();
				JSONObject json = (JSONObject) jsonArray.get(j);
				movie.setRate(json.getDouble("rate")); //取评分
				movie.setTitle(json.getString("title")); //取电影名称
				//可取其他信息
				//取详细信息url
				String uurl=json.getString("url");
				//取得详细信息的页面
				String html2 = HttpUtils.doGetHtml(uurl, parameter2, header2);
				//解析
				Document document = Jsoup.parse(html2);
				Element directedBy = document.select("div#info a[rel=v:directedBy]").first();
				Element summary = document.select("div#link-report span[property=v:summary]").first();
				//System.out.println(element.text());//打印导演
				movie.setDirectedBy(directedBy.text());
				movie.setSummary(summary.text());
				System.out.println("电影:"+movie.getTitle()+"  评分:"+movie.getRate() +"   导演:"+movie.getDirectedBy());
				System.out.println("简介："+movie.getSummary());

				//可以在此处使用业务对象进行数据库持久化

			}
		}
		System.out.println("数据获取完成");
	}
}
