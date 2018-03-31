package pl.piomin.microservices.gps.model;

/**
 * 轨迹记录信息  mongodb
 */
public class Order_track {
    public String order_id;//订单信息 任务信息
    public String time;//信息采集时间
    public float lon;//经度
    public float lat;//维度
    public float speed;//速度
    public float direction;//方向角（度）
    public int state;//0静止，1移动，2未知
    public String point;//接单 取餐 送餐中 送达 回程 结束

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
