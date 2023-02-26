package test.API.posts.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Profile("dev")
@Configuration
@EnableJpaRepositories(
    basePackages = { DbConfigurationDev.SCAN_PACKAGE_DAO},
    entityManagerFactoryRef = DbConfigurationDev.ENTITY_MANAGER
)
public class DbConfigurationDev {

  public static final String TRANSACTION_MANAGER = "transactionManager";
  public static final String ENTITY_MANAGER = "entityManager";
  public static final String SCAN_PACKAGE_DAO = "test.API.posts";
  public static final String DATASOURCE = "dataSource";

  @Bean(name = DATASOURCE)
  @Primary
  public DataSource dataSource () {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();

    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://localhost:5432/posts");
    dataSource.setUsername("postgres");
    dataSource.setPassword("123321");

    return dataSource;
  }

  @Bean(name = ENTITY_MANAGER)
  @Primary
  @Autowired
  public LocalContainerEntityManagerFactoryBean entityManager (@Qualifier(DATASOURCE) DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan(SCAN_PACKAGE_DAO);

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    HashMap<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put("hibernate.hbm2ddl.auto", "create");
    jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    jpaProperties.put("hibernate.format_sql", false);
    jpaProperties.put("hibernate.show_sql", true);
    jpaProperties.put("hibernate.jdbc.time_zone", "UTC");
    jpaProperties.put("org.hibernate.flushMode", "COMMIT");
    jpaProperties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
    em.setJpaPropertyMap(jpaProperties);

    return em;
  }

  @Bean(name = TRANSACTION_MANAGER)
  @Primary
  @Autowired
  JpaTransactionManager transactionManager (@Qualifier(DATASOURCE) DataSource dataSource, @Qualifier(ENTITY_MANAGER) EntityManagerFactory entityManager) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(dataSource);
    transactionManager.setEntityManagerFactory(entityManager);

    return transactionManager;
  }


}
