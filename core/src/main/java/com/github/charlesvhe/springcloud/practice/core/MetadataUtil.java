package com.github.charlesvhe.springcloud.practice.core;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MetadataUtil {

    public static final String META_DATA_KEY_LABEL_AND = "labelAnd";
    public static final String META_DATA_KEY_LABEL_OR = "labelOr";

    public static final String META_DATA_KEY_WEIGHT = "weight";

    public static boolean checkLabelOr(Map<String, String> metadata){
        String labelOr = metadata.get(META_DATA_KEY_LABEL_OR);

        if(!StringUtils.isEmpty(labelOr)){
            List<String> metadataLabel = Arrays.asList(labelOr.split(CoreHeaderInterceptor.HEADER_LABEL_SPLIT));
            for (String label : metadataLabel) {
                if(CoreHeaderInterceptor.label.get().contains(label)){
                    System.out.println("*****  MetadataAwarePredicate  ******"+labelOr+"***********  true");
                    return true;
                }
            }
        }
        System.out.println("*****  MetadataAwarePredicate  ******"+labelOr+"***********  false");
        return false;
    }

    public static boolean checkLabelAnd(Map<String, String> metadata){
        String labelOr = metadata.get(META_DATA_KEY_LABEL_OR);

        String labelAnd = metadata.get(META_DATA_KEY_LABEL_AND);
        if(!StringUtils.isEmpty(labelAnd)){
            List<String> metadataLabel = Arrays.asList(labelAnd.split(CoreHeaderInterceptor.HEADER_LABEL_SPLIT));
            if(CoreHeaderInterceptor.label.get().containsAll(metadataLabel)){
                return true;
            }
        }
        return false;
    }

}
