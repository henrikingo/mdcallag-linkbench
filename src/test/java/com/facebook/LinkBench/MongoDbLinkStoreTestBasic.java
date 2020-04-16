package com.facebook.LinkBench;

import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.util.Properties;


/**
 * Test the MongoDB LinkStore implementation.
 *
 * Assumes that the database specified by the testDB field has been created
 * with permissions for a user/pass linkbench/linkbench to create tables, select,
 * insert, delete, etc.
 */
public class MongoDbLinkStoreTestBasic extends LinkStoreTestBase {

    private MongoDatabase conn;

    /** Properties for last initStore call */
    private Properties currProps;

    @Override
    protected long getIDCount() {
        // Make test smaller so that it doesn't take too long
        return 2500;
    }

    @Override
    protected int getRequestCount() {
        // Fewer requests to keep test quick
        return 10000;
    }

    protected Properties basicProps() {
        Properties props = super.basicProps();
        MongoDbTestConfigBasic.fillMongoDbTestServerProps(props);
        return props;
    }


    @Override
    protected void initStore(Properties props) throws IOException, Exception {
        this.currProps = (Properties)props.clone();
        conn = MongoDbTestConfigBasic.createConnection(testDB);
        MongoDbTestConfigBasic.dropTestTables(conn, testDB);
        MongoDbTestConfigBasic.createTestTables(conn, testDB);
    }



    @Override
    public DummyLinkStore getStoreHandle(boolean initialize) throws IOException, Exception {
        DummyLinkStore result = new DummyLinkStore(new LinkStoreMongoDbBasic());
        if (initialize) {
            result.initialize(currProps, Phase.REQUEST, 0);
        }
        return result;
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
        MongoDbTestConfigBasic.dropTestTables(conn, testDB);
        conn = null;
    }

}
