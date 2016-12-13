package com.lyl.haza.httprsp;

import com.lyl.haza.bean.NewsBean;

import java.util.List;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class NewsRsp extends BaseRsp {

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        private String stat;
        private List<NewsBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<NewsBean> getData() {
            return data;
        }

        public void setData(List<NewsBean> data) {
            this.data = data;
        }
    }
}
