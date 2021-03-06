package org.parc.sqlrestes.query.multi;

import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.parc.restes.RestQueryBuilder;
import org.parc.sqlrestes.domain.Field;
import org.parc.sqlrestes.domain.Select;
import org.parc.sqlrestes.query.SqlElasticRequestBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eliran on 19/8/2016.
 */
public class MultiQueryRequestBuilder implements SqlElasticRequestBuilder {

    private RestQueryBuilder firstSearchRequest;
    private RestQueryBuilder secondSearchRequest;
    private Map<String, String> firstTableFieldToAlias;
    private Map<String, String> secondTableFieldToAlias;
    private MultiQuerySelect multiQuerySelect;
    private SQLUnionOperator relation;


    public MultiQueryRequestBuilder(MultiQuerySelect multiQuerySelect) {
        this.multiQuerySelect = multiQuerySelect;
        this.relation = multiQuerySelect.getOperation();
        this.firstTableFieldToAlias = new HashMap<>();
        this.secondTableFieldToAlias = new HashMap<>();
    }

    @Override
    public ActionRequest request() {
        return null;
    }


    @Override
    public String explain() {

        try {
            XContentBuilder firstBuilder = XContentFactory.contentBuilder(XContentType.JSON).prettyPrint();
//            this.firstSearchRequest.request().source().toXContent(firstBuilder, ToXContent.EMPTY_PARAMS);

            XContentBuilder secondBuilder = XContentFactory.contentBuilder(XContentType.JSON).prettyPrint();
//            this.secondSearchRequest.request().source().toXContent(secondBuilder, ToXContent.EMPTY_PARAMS);

            return String.format("performing %s on :\n left query:\n%s\n right query:\n%s", this.relation.name, firstBuilder.string(), secondBuilder.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ActionResponse get() {
        return null;
    }

    @Override
    public ActionRequestBuilder getBuilder() {
        return null;
    }


    public RestQueryBuilder getFirstSearchRequest() {
        return firstSearchRequest;
    }

    public void setFirstSearchRequest(RestQueryBuilder firstSearchRequest) {
        this.firstSearchRequest = firstSearchRequest;
    }

    public RestQueryBuilder getSecondSearchRequest() {
        return secondSearchRequest;
    }

    public void setSecondSearchRequest(SearchRequestBuilder secondSearchRequest) {
//        this.secondSearchRequest = secondSearchRequest;
    }

    public SQLUnionOperator getRelation() {
        return relation;
    }

    public void fillTableAliases(List<Field> firstTableFields, List<Field> secondTableFields) {
        fillTableToAlias(this.firstTableFieldToAlias, firstTableFields);
        fillTableToAlias(this.secondTableFieldToAlias, secondTableFields);
    }

    private void fillTableToAlias(Map<String, String> fieldToAlias, List<Field> fields) {
        for (Field field : fields) {
            if (field.getAlias() != null && !field.getAlias().isEmpty()) {
                fieldToAlias.put(field.getName(), field.getAlias());
            }
        }
    }

    public Map<String, String> getFirstTableFieldToAlias() {
        return firstTableFieldToAlias;
    }

    public Map<String, String> getSecondTableFieldToAlias() {
        return secondTableFieldToAlias;
    }

    public Select getOriginalSelect(boolean first) {
        if (first) {
            return this.multiQuerySelect.getFirstSelect();
        } else {
            return this.multiQuerySelect.getSecondSelect();
        }
    }
}
