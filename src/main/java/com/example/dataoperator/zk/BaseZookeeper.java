package com.example.dataoperator.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * java工具类直接连接zk，springboot继承则不需如此
 */
public class BaseZookeeper implements Watcher {

    private ZooKeeper zookeeper;

    /**
     * 超时时间
     */
    private static final int SESSION_TIME_OUT = 2000;
    /**
     * 服务端IP和端口
     */
    private static final String IP_PORT = "192.168.35.1:2181";


    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Watch received event");
            countDownLatch.countDown();
        }
    }


    /**
     * 连接zookeeper
     *
     * @throws Exception
     */
    public void connectZookeeper() throws Exception {
        zookeeper = new ZooKeeper(IP_PORT, SESSION_TIME_OUT, this);
        countDownLatch.await();
        System.out.println("zookeeper connection success");
    }

    /**
     * 创建节点
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public String createNode(String path, String data) throws Exception {
        return this.zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 获取路径下所有子节点
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zookeeper.getChildren(path, false);
        return children;
    }

    /**
     * 获取节点上面的数据
     *
     * @param path 路径
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getData(String path) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, false, null);
        if (data == null) {
            return "";
        }
        return new String(data);
    }

    /**
     * 设置节点信息
     *
     * @param path 路径
     * @param data 数据
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat setData(String path, String data) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.setData(path, data.getBytes(), -1);
        return stat;
    }

    /**
     * 删除节点
     *
     * @param path
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void deleteNode(String path) throws InterruptedException, KeeperException {
        zookeeper.delete(path, -1);
    }

    /**
     * 获取创建时间
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getCTime(String path) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.exists(path, false);
        return String.valueOf(stat.getCtime());
    }

    /**
     * 获取某个路径下孩子的数量
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Integer getChildrenNum(String path) throws KeeperException, InterruptedException {
        int childenNum = zookeeper.getChildren(path, false).size();
        return childenNum;
    }

    /**
     * 关闭连接
     *
     * @throws InterruptedException
     */
    public void closeConnection() throws InterruptedException {
        if (zookeeper != null) {
            zookeeper.close();
        }
    }


    public static void main(String[] args) throws Exception {
        BaseZookeeper zookeeper = new BaseZookeeper();
        zookeeper.connectZookeeper();
        zookeeper.setData("/a/aa", "aaab");

        List<String> children = zookeeper.getChildren("/a");
        System.out.println("children:"+children);
        System.out.println(zookeeper.getData("/a/aa"));
    }


}




