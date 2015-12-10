package com.zhaohuo18.api.module;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.zhaohuo18.api.vo.ProductVO;

public class MainModule {

	@At("/")
    @Ok("raw")
    public String doHello(@Param("url") String url) {
		ProductVO pvo = fetchW(url);
		if(pvo != null){
			return Json.toJson(pvo);
		}
        return "Hello Nutz";
    }
	@At("/split")
    @Ok("raw")
    public String doSplit(@Param("text") String text) {
		List<String> list = null;
		if(StringUtils.isNotEmpty(text)){
			try{
				list = split(text);
				return Json.toJson(list);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
        return "Hello Nutz";
    }
	@At("/fetchUrl")
    @Ok("raw")
    public String doFetch(String url) {
		ProductVO pvo = fetchW(url);
		if(pvo != null){
			return Json.toJson(pvo);
		}
        return "Hello Nutz";
    }
	
	private ProductVO fetchW(final String url){
		ProductVO pvo = null;
		try{
			if(StringUtils.isEmpty(url)){
				return pvo;
			}
			Document doc = Jsoup.connect(url).get();
			if(url.contains("tmall.com")){
				pvo = fetchTmall(doc);
			}else if(url.contains("taobao.com")){
				pvo = fetchTaobao(doc);
			}else if(url.contains("yhd.com")){
				pvo = fetchYhd(doc);
			}else if(url.contains("jd.com")){
				pvo = fetchJd(doc);
			}else{
				pvo = fetchJd(doc);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return pvo;
	}
	private ProductVO fetchYhd(Document doc){
		ProductVO pvo = new ProductVO();
		String title = doc.title();
		pvo.setTitle(title);
		Elements eles = doc.select("div.proCrumb");
		for(Element e:eles.select("img")){
			String picurl = e.attr("src").replace("50x50", "360x360");
			pvo.getPiclist().add(picurl);
		}
		return pvo;
	}
	private ProductVO fetchTmall(Document doc){
		ProductVO pvo = new ProductVO();
		String title = doc.title();
		pvo.setTitle(title);
		Elements eles = doc.select("ul#J_UlThumb");
		for(Element e:eles.select("img")){
			String picurl = "http:" + e.attr("src").replace("60x60", "360x360");
			pvo.getPiclist().add(picurl);
		}
		return pvo;
	}
	private ProductVO fetchTaobao(Document doc){
		ProductVO pvo = new ProductVO();
		String title = doc.title();
		pvo.setTitle(title);
		Elements eles = doc.select("ul#J_UlThumb");
		for(Element e:eles.select("img")){
			String picurl = "http:" + e.attr("data-src").replace("50x50", "360x360");
			pvo.getPiclist().add(picurl);
		}
		return pvo;
	}
	private ProductVO fetchJd(Document doc){
		ProductVO pvo = new ProductVO();
		String title = doc.title();
		pvo.setTitle(title);
		Elements eles = doc.select("div.spec-items");
		for(Element e:eles.select("img")){
			String picurl = "http:" + e.attr("src").replace("n5", "n1");
			pvo.getPiclist().add(picurl);
		}
		return pvo;
	}
	private ProductVO fetchMTO(Document doc){
		ProductVO pvo = new ProductVO();
		String title = doc.title();
		pvo.setTitle(title);
		Elements eles = doc.select("div#spec-list");
		for(Element e:eles.select("img")){
			String picurl = "http://www.metromall.cn" + e.attr("bigpic");
			pvo.getPiclist().add(picurl);
		}
		return pvo;
	}
	private List<String> split(String text) throws IOException{
		List<String> retlist = new ArrayList<String>();
		StringReader sr = new StringReader(text);
		IKSegmenter ik = new IKSegmenter(sr, true);
		Lexeme lex = null;
		while ((lex = ik.next()) != null) {
			//System.out.print(lex.getLexemeText() + "|");
			retlist.add(lex.getLexemeText());
		}
		return retlist;
	}
}
