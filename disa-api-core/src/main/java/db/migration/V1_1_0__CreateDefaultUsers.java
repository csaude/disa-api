package db.migration;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.flywaydb.core.api.migration.spring.BaseSpringJdbcMigration;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class V1_1_0__CreateDefaultUsers extends BaseSpringJdbcMigration {

    private static final String PASSWORD_FILE = "passwd";
    private static final String IMPLEMENTATION_REPORT_CSV = "Implementation_report.csv";
    private static final int IP_COLUMN = 0;
    private static final int OU_COLUMN = 4;
    private static final String INSERT_QUERY = "insert into ImplementingPartner(orgname,password,enabled) values (?, ?, ?);";
    private static final String DELETE_QUERY = "delete from ImplementingPartner where orgname = ?;";
    private static final String UPDATE_ORGUNITS_QUERY = "update OrgUnit set implementingPartnerId = (select id from ImplementingPartner where orgname='%s') where code in (%s);";

    private static final String[] usernames = new String[] {
            "jhpiego",
            "egpaf",
            "echo",
            "ccs",
            "ariel",
            "icap",
            "fgh"
    };

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        setJdbcTemplate(jdbcTemplate);

        Map<String, Set<String>> orgUnits = loadOrgUnits();

        String username = "";
        try (OutputStream pwdFileStream = Files.newOutputStream(Paths.get(PASSWORD_FILE), CREATE, APPEND)) {

            for (String u : usernames) {
                username = u;
                createUser(username, orgUnits.getOrDefault(username, new HashSet<>()), pwdFileStream);
            }

        } catch (IOException e) {
            deleteUser(username);
            e.printStackTrace();
        }

    }

    private Map<String, Set<String>> loadOrgUnits() throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(IMPLEMENTATION_REPORT_CSV))) {
            return lines
                    .skip(1)
                    .map(l -> l.split(","))
                    .collect(Collectors.groupingBy(split -> split[IP_COLUMN].toLowerCase(),
                            Collectors.mapping(split -> split[OU_COLUMN], Collectors.toSet())));
        }

    }

    private void createUser(String username, Set<String> orgUnits, OutputStream pwdFileStream) throws IOException {

        String password = generateRandomPassword();
        createUser(User
                .withUsername(username)
                .password(passwordEncoder().encode(password))
                .roles("USER")
                .build());

        if (!orgUnits.isEmpty()) {
            updateOrgUnits(username, orgUnits);
        }

        pwdFileStream.write((username + ":" + password + "\n").getBytes());
    }

    private void updateOrgUnits(String username, Set<String> orgUnits) {
        jdbcTemplate.update(String.format(UPDATE_ORGUNITS_QUERY, username, String.join(",", orgUnits)));
    }

    private void createUser(UserDetails userDetails) {
        jdbcTemplate.update(INSERT_QUERY, userDetails.getUsername(), userDetails.getPassword(), true);
    }

    private void deleteUser(String username) {
        jdbcTemplate.update(DELETE_QUERY, username);
    }

    private String generateRandomPassword() {
        List<CharacterRule> rules = Arrays.asList(
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1));

        PasswordGenerator generator = new PasswordGenerator();

        // Generated password is 12 characters long, which complies with policy
        return generator.generatePassword(12, rules);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
