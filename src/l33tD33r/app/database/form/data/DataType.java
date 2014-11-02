package l33tD33r.app.database.form.data;

import l33tD33r.app.database.Date;

/**
 * Created by Simon on 2014-10-28.
 */
public enum DataType {
    String {
        @Override
        public Object parse(String stringValue) {
            return stringValue;
        }
    },
    Reference {
        @Override
        public Object parse(String stringValue) {
            return stringValue;
        }
    },
    Number {
        @Override
        public Object parse(String stringValue) {
            return Double.valueOf(stringValue);
        }
    },
    Integer {
        @Override
        public Object parse(String stringValue) {
            return java.lang.Integer.valueOf(stringValue);
        }
    },
    Date {
        @Override
        public Object parse(String stringValue) {
            return l33tD33r.app.database.Date.valueOf(stringValue);
        }
    },
    Boolean {
        @Override
        public Object parse(String stringValue) {
            return java.lang.Boolean.valueOf(stringValue);
        }
    };

    public abstract Object parse(String stringValue);
}
