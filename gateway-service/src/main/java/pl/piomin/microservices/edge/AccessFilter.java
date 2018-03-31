package pl.piomin.microservices.edge;

import com.github.charlesvhe.springcloud.practice.core.CoreHeaderInterceptor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        try {
            StringBuffer sb = new StringBuffer() ;

            InputStream is = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));

            String s = "" ;
            while((s = br.readLine()) != null){
                sb.append(s);
            }
            System.out.println("#### sb.toString() ####"+sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tag = request.getParameter("tag");
        String labels = request.getParameter("labels");

        System.out.println("########"+tag);
        System.out.println("#### labels ####"+labels);
        //灰度示例
        //
//        if(StringUtils.isEmpty(labels)){
//        }else {
//            RibbonFilterContextHolder.getCurrentContext().add("labelOr", labels);
//        }

//        if (token.equals("1234567890")) {
//            RibbonFilterContextHolder.getCurrentContext().add("gated-launch", "true");
//        } else {
//            RibbonFilterContextHolder.getCurrentContext().add("gated-launch", "false");
//        }

        CoreHeaderInterceptor.initHystrixRequestContext(labels); // zuul本身调用微服务
        ctx.addZuulRequestHeader(CoreHeaderInterceptor.HEADER_LABEL, labels); // 传递给后续微服务















       String traceId = TraceContext.traceId();
       System.out.println("###########///////////#### tracId ####"+traceId);

        ActiveSpan.tag("service","统一网关");
        return null;
    }

}
