import com.fferreira.example.hazelcast.User;
import com.fferreira.example.hazelcast.mapstore.HazelcastMapStore;
import com.fferreira.example.hazelcast.mapstore.cassandra.CassandraClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import java.util.Properties;
import java.util.UUID;

public class InstanceStart {
  public static void main(String[] args) throws InterruptedException {
    final Config cfg = new Config();
    cfg.setInstanceName(UUID.randomUUID().toString());

    final Properties props = new Properties();
    props.put("hazelcast.rest.enabled", false);
    props.put("hazelcast.logging.type", "slf4j");
    props.put("hazelcast.connect.all.wait.seconds", 45);
    props.put("hazelcast.operation.call.timeout.millis", 30000);

    // group configuration
    cfg.setGroupConfig(new GroupConfig(args[0],
        args[1]));
    // network configuration initialization
    final NetworkConfig netCfg = new NetworkConfig();
    netCfg.setPortAutoIncrement(true);
    netCfg.setPort(5701);
    // multicast
    final MulticastConfig mcCfg = new MulticastConfig();
    mcCfg.setEnabled(false);
    // tcp
    final TcpIpConfig tcpCfg = new TcpIpConfig();
    tcpCfg.addMember("127.0.0.1");
    tcpCfg.setEnabled(true);
    // network join configuration
    final JoinConfig joinCfg = new JoinConfig();
    joinCfg.setMulticastConfig(mcCfg);
    joinCfg.setTcpIpConfig(tcpCfg);
    netCfg.setJoin(joinCfg);
    // ssl
    netCfg.setSSLConfig(new SSLConfig().setEnabled(false));

    // creating cassandra client
    final CassandraClient dao = new CassandraClient();
    dao.initialize(args[2]);

    final HazelcastMapStore mapStore = new HazelcastMapStore(User.class);
    mapStore.setDao(dao);


    // Adding mapstore
    final MapConfig mapCfg = cfg.getMapConfig("cassandra-map-store");

    final MapStoreConfig mapStoreCfg = new MapStoreConfig();
    mapStoreCfg.setImplementation(mapStore);
    mapStoreCfg.setWriteDelaySeconds(1);
    // to load all map at same time
    mapStoreCfg.setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER);
    mapCfg.setMapStoreConfig(mapStoreCfg);
    cfg.addMapConfig(mapCfg);


    HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);

    // TERM signal processing
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      Hazelcast.shutdownAll();
    }));
  }
}