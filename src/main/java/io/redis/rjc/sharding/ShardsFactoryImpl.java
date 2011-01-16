package io.redis.rjc.sharding;

import io.redis.rjc.NodeFactory;
import io.redis.rjc.ds.DataSourceFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Evgeny Dolgov
 */
public class ShardsFactoryImpl<T> implements ShardsFactory<T> {

    private NodeFactory<T> nodeFactory;
    private DataSourceFactory dataSourceFactory;
    private String addresses;
    private String weights;
    private String ids;

    public Collection<Shard<T>> create() {
        if (addresses == null) {
            throw new IllegalStateException("Addresses must not be null");
        }

        final String[] addressesArr = addresses.split(",");
        final int[] weightsArr = extractWeights(addressesArr);
        final String[] idsArr = extractIds(addressesArr);

        final Collection<Shard<T>> result = new ArrayList<Shard<T>>(addressesArr.length);
        for (int i = 0; i < addressesArr.length; i++) {
            String address = addressesArr[i].trim();
            String[] hostPort = address.split(":");
            result.add(new ShardImpl<T>(
                    idsArr[i].trim(),
                    nodeFactory.create(dataSourceFactory.create(hostPort[0].trim(), Integer.parseInt(hostPort[1].trim()))),
                    weightsArr[i])
            );
        }


        return result;
    }

    private String[] extractIds(String[] addressesArr) {
        final String[] idsArr;
        if (ids == null) {
            idsArr = new String[addressesArr.length];
            System.arraycopy(addressesArr, 0, idsArr, 0, idsArr.length);
        } else {
            idsArr = ids.split(",");
            if (idsArr.length != addressesArr.length) {
                throw new IllegalStateException("Number of addresses and ids must be equal");
            }
        }
        return idsArr;
    }

    private int[] extractWeights(String[] addressesArr) {
        final int[] weightsArr = new int[addressesArr.length];
        if (weights == null) {
            for (int i = 0; i < weightsArr.length; i++) {
                weightsArr[i] = 1;
            }
        } else {
            String[] weightsStrArr = weights.split(",");
            if (weightsStrArr.length != addressesArr.length) {
                throw new IllegalStateException("Number of addresses and weights must be equal");
            }

            for (int i = 0; i < weightsStrArr.length; i++) {
                weightsArr[i] = Integer.parseInt(weightsStrArr[i].trim());
            }
        }
        return weightsArr;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public NodeFactory<T> getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(NodeFactory<T> nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }
}
