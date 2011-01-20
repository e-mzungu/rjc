/*
 * Copyright 2010-2011. Evgeny Dolgov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.idevlab.rjc.sharding;

import org.idevlab.rjc.NodeFactory;
import org.idevlab.rjc.ds.DataSource;
import org.idevlab.rjc.ds.DataSourceFactory;
import org.idevlab.rjc.ds.RedisConnection;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Evgeny Dolgov
 */
public class ShardsFactoryTest {

    private NodeFactory nodeFactoryMock;
    private DataSourceFactory dataSourceFactoryMock;
    private ShardsFactoryImpl<TestNode> shardsFactory;


    @SuppressWarnings({"unchecked"})
    @Before
    public void setUp() {
        nodeFactoryMock = createMock(NodeFactory.class);
        dataSourceFactoryMock = createMock(DataSourceFactory.class);
        shardsFactory = new ShardsFactoryImpl<TestNode>();
        shardsFactory.setDataSourceFactory(dataSourceFactoryMock);
        shardsFactory.setNodeFactory(nodeFactoryMock);
    }

    @Test
    public void onlyAddressTest() {
        shardsFactory.setAddresses("addr1:1, addr2:2");
        twoAddressesTest("addr1:1", "addr2:2", 1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void noAddressTest() {
        twoAddressesTest("addr1:1", "addr2:2", 1, 1);
    }

    @Test
    public void addressAndIdsTest() {
        shardsFactory.setAddresses("addr1:1, addr2:2");
        shardsFactory.setIds("shard1, shard2");
        twoAddressesTest("shard1", "shard2", 1, 1);
    }

    @Test
    public void addressAndWeightTest() {
        shardsFactory.setAddresses("addr1:1, addr2:2");
        shardsFactory.setWeights("2, 5");
        twoAddressesTest("addr1:1", "addr2:2", 2, 5);
    }

    @Test(expected = IllegalStateException.class)
    public void addressAndWrongIdsTest() {
        shardsFactory.setAddresses("addr1:1, addr2:2");
        shardsFactory.setIds("shard1");
        twoAddressesTest("shard1", "shard2", 1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void addressAndWrongWeightTest() {
        shardsFactory.setAddresses("addr1:1, addr2:2");
        shardsFactory.setWeights("2");
        twoAddressesTest("addr1:1", "addr2:2", 2, 5);
    }

    private void twoAddressesTest(String shard1Id, String shard2Id, int node1Weight, int node2Weight) {

        DataSource testDataSource = new DataSource() {
            public RedisConnection getConnection() {
                return null;
            }
        };

        TestNode node1 = new TestNode();
        TestNode node2 = new TestNode();

        expect(dataSourceFactoryMock.create("addr1", 1)).andReturn(testDataSource);
        expect(nodeFactoryMock.create(testDataSource)).andReturn(node1);

        expect(dataSourceFactoryMock.create("addr2", 2)).andReturn(testDataSource);
        expect(nodeFactoryMock.create(testDataSource)).andReturn(node2);

        replay(nodeFactoryMock, dataSourceFactoryMock);
        List<Shard<TestNode>> shards = new ArrayList<Shard<TestNode>>(shardsFactory.create());
        verify(nodeFactoryMock, dataSourceFactoryMock);

        Shard<TestNode> shard = shards.get(0);
        assertEquals(node1, shard.getNode());
        assertEquals(shard1Id, shard.getShardId());
        assertEquals(node1Weight, shard.getWeight());

        shard = shards.get(1);
        assertEquals(node2, shard.getNode());
        assertEquals(shard2Id, shard.getShardId());
        assertEquals(node2Weight, shard.getWeight());
    }

    private static class TestNode {
    }
}
