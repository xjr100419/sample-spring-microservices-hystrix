package pl.piomin.microservices.edge;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;


public class ErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes (
            RequestAttributes requestAttributes, boolean includeStackTrace){
        Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
        result.remove("exception");
        result.put("error","missing error");
        System.out.println("###### getErrorAttributes......");
        return result;
    }

}
