package zjc.examples.r2dbc;

import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import zjc.examples.r2dbc.configuration.R2DBCConfiguration;
import zjc.examples.r2dbc.model.Player;
import zjc.examples.r2dbc.repository.PlayerRepository;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = R2DBCConfiguration.class)
public class R2dbcApplicationIntegrationTest {


    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    DatabaseClient client;

    @Autowired
    PostgresqlConnectionFactory factory;


    @Before
    public void setup() {

        Hooks.onOperatorDebug();

        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS player;",
                "CREATE table player (id SERIAL PRIMARY KEY, name VARCHAR(255), age INT NOT NULL);");

        statements.forEach(it -> client.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    @Test
    public void deleteAllPlayers() {
        playerRepository.deleteAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void insert_8_Players() {
        List<Player> players = Arrays.asList(
                new Player(null, "Alford", 34),
                new Player(null, "Cage", 22),
                new Player(null, "Haward", 41),
                new Player(null, "Indiana", 35),
                new Player(null, "Rowan", 53),
                new Player(null, "Sudie", 32),
                new Player(null, "Norton", 32),
                new Player(null, "Lee", 42)
        );

        try {
            playerRepository.saveAll(players)
                    .as(StepVerifier::create)
                    .expectNextCount(8)
                    .verifyComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_1_Player() {
        Player player = new Player(null, "aaa", 45);
        try {
            playerRepository.save(player)
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findAllPlayers_test1() {
        deleteAllPlayers();
        insert_1_Player();
        playerRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findAllPlayers_test2() {
        deleteAllPlayers();
        insert_1_Player();
        insert_8_Players();
        playerRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    public void findAllByName_test1() {
        deleteAllPlayers();
        insert_1_Player();
        insert_8_Players();
        playerRepository.findAllByName("Sudie")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findByAge_test1() {
        deleteAllPlayers();
        insert_1_Player();
        insert_8_Players();
        playerRepository.findByAge(32)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void createBatch_test1() {
        Mono.from(factory.create())
                .flatMapMany(connection -> Flux.from(connection
                        .createBatch()
                        .add("select * from player")
                        .add("select * from player")
                        .add("select * from player")
                        .execute()))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }
}
