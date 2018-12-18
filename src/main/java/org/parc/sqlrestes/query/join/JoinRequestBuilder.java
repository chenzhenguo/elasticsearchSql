package org.parc.sqlrestes.query.join;

import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.parc.sqlrestes.query.SqlElasticRequestBuilder;

import java.io.IOException;

/**
 * Created by Eliran on 15/9/2015.
 */
public class JoinRequestBuilder implements SqlElasticRequestBuilder {

    private MultiSearchRequest multi;
    private TableInJoinRequestBuilder firstTable;
    private TableInJoinRequestBuilder secondTable;
    private SQLJoinTableSource.JoinType joinType;
    private int totalLimit;

    JoinRequestBuilder() {
        firstTable = new TableInJoinRequestBuilder();
        secondTable = new TableInJoinRequestBuilder();
    }


    @Override
    public ActionRequest request() {
        if (multi == null) {
            buildMulti();
        }
        return multi;

    }

    private void buildMulti() {
        multi = new MultiSearchRequest();
//        multi.add(firstTable.getRequestBuilder());
//        multi.add(secondTable.getRequestBuilder());
    }

    @Override
    public String explain() {
        try {
            XContentBuilder firstBuilder = XContentFactory.contentBuilder(XContentType.JSON).prettyPrint();
//            firstTable.getRequestBuilder().request().source().toXContent(firstBuilder, ToXContent.EMPTY_PARAMS);
            firstTable.getRequestBuilder().toString();

            XContentBuilder secondBuilder = XContentFactory.contentBuilder(XContentType.JSON).prettyPrint();
//            secondTable.getRequestBuilder().request().source().toXContent(secondBuilder, ToXContent.EMPTY_PARAMS);
            secondTable.getRequestBuilder().toString();

            return String.format(" first query:\n%s\n second query:\n%s", firstBuilder.string(), secondBuilder.string());
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
//        return this.firstTable.getRequestBuilder();
        return null;
    }

    public MultiSearchRequest getMulti() {
        return multi;
    }

    public void setMulti(MultiSearchRequest multi) {
        this.multi = multi;
    }

    public SQLJoinTableSource.JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(SQLJoinTableSource.JoinType joinType) {
        this.joinType = joinType;
    }

    public TableInJoinRequestBuilder getFirstTable() {
        return firstTable;
    }

    public TableInJoinRequestBuilder getSecondTable() {
        return secondTable;
    }

    public int getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(int totalLimit) {
        this.totalLimit = totalLimit;
    }

}
