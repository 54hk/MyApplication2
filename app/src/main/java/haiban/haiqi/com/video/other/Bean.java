package haiban.haiqi.com.video.other;

import java.util.List;

/**
 * Created by 54hk on 2017/12/12.
 */

public class Bean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name_cn : 安道尔
         * name_tw : 安道爾
         * name : Andorra
         * code : AD
         * l1 : [{"name_cn":"阿布扎比","name_tw":"阿布扎比","name":"Abu Dhabi"},{"name_cn":"艾因","name_tw":"艾因","name":"Al l'Ayn"},{"name_cn":"沙迦","name_tw":"沙迦","name":"Ash Shariqah"},{"name_cn":"迪拜","name_tw":"迪拜","name":"Dubai"}]
         */

        private String name_cn;
        private String name_tw;
        private String name;
        private String code;
        private List<L1Bean> l1;

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }

        public String getName_tw() {
            return name_tw;
        }

        public void setName_tw(String name_tw) {
            this.name_tw = name_tw;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<L1Bean> getL1() {
            return l1;
        }

        public void setL1(List<L1Bean> l1) {
            this.l1 = l1;
        }

        public static class L1Bean {
            /**
             * name_cn : 阿布扎比
             * name_tw : 阿布扎比
             * name : Abu Dhabi
             */

            private String name_cn;
            private String name_tw;
            private String name;

            public String getName_cn() {
                return name_cn;
            }

            public void setName_cn(String name_cn) {
                this.name_cn = name_cn;
            }

            public String getName_tw() {
                return name_tw;
            }

            public void setName_tw(String name_tw) {
                this.name_tw = name_tw;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
