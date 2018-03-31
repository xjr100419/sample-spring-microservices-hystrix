package pl.piomin.microservices.gps.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * GIS 地理信息 服务 业务
 * 1.收集 移动端 传回来的 gps 信息。
 *      1.1 信息内容：订单信息 ，地理信息（gps）,时间
 *      1.2 信息储存：更新最新地理信息 到redis,同时插入一条记录到mongodb
 *
 *
 * 2.提供 订单 gps 信息查询，（1）最新地理信息（2）地理信息跟踪
 *      2.1 根据订单号 查询地理信息 ，先查redis 没有就查 mongodb
 *      2.2 根据订单号 查询订单轨迹 查询mongodb
 */
@RestController
public class GISController {

      @RequestMapping(value = "/gps/receive")
      public Map receiveGps(){
          Map result = new HashMap();


          return result;
      }



}
