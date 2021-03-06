package org.parc.sqlrestes.entity;


import org.elasticsearch.common.Strings;
import org.parc.sqlrestes.entity.log.DeprecationLogger;
import org.parc.sqlrestes.entity.log.Loggers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by xusiao on 2018/5/4.
 */
public class ParseField {
    private static final DeprecationLogger DEPRECATION_LOGGER = new DeprecationLogger(Loggers.getLogger(ParseField.class));
    private final String name;
    private final String[] deprecatedNames;
    private final String[] allNames;
    private String allReplacedWith = null;

    public ParseField(String name, String... deprecatedNames) {
        this.name = name;
        HashSet allNames;
        if (deprecatedNames != null && deprecatedNames.length != 0) {
            allNames = new HashSet();
            Collections.addAll(allNames, deprecatedNames);
            this.deprecatedNames = (String[]) allNames.toArray(new String[0]);
        } else {
            this.deprecatedNames = Strings.EMPTY_ARRAY;
        }

        allNames = new HashSet();
        allNames.add(name);
        Collections.addAll(allNames, this.deprecatedNames);
        this.allNames = (String[]) allNames.toArray(new String[0]);
    }

    public String getPreferredName() {
        return this.name;
    }

    private String[] getAllNamesIncludedDeprecated() {
        return this.allNames;
    }

    private ParseField withDeprecation(String... deprecatedNames) {
        return new ParseField(this.name, deprecatedNames);
    }

    public ParseField withAllDeprecated(String allReplacedWith) {
        ParseField parseField = this.withDeprecation(this.getAllNamesIncludedDeprecated());
        parseField.allReplacedWith = allReplacedWith;
        return parseField;
    }

    public boolean match(String fieldName) {
        Objects.requireNonNull(fieldName, "fieldName cannot be null");
        if (this.allReplacedWith == null && fieldName.equals(this.name)) {
            return true;
        } else {
            String[] var3 = this.deprecatedNames;

            for (String depName : var3) {
                if (fieldName.equals(depName)) {
                    String msg = "Deprecated field [" + fieldName + "] used, expected [" + this.name + "] instead";
                    if (this.allReplacedWith != null) {
                        msg = "Deprecated field [" + fieldName + "] used, replaced by [" + this.allReplacedWith + "]";
                    }

                    DEPRECATION_LOGGER.deprecated(msg);
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public String toString() {
        return this.getPreferredName();
    }

    public String getAllReplacedWith() {
        return this.allReplacedWith;
    }

    public String[] getDeprecatedNames() {
        return this.deprecatedNames;
    }

    private static class CommonFields {
        public static final ParseField FIELD = new ParseField("field");
        public static final ParseField FIELDS = new ParseField("fields");
        public static final ParseField FORMAT = new ParseField("format");
        public static final ParseField MISSING = new ParseField("missing");
        public static final ParseField TIME_ZONE = new ParseField("time_zone");

        public CommonFields() {
        }
    }
}
