package com.pocdatasource.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "postgresqlEntityManager", 
		transactionManagerRef = "postgresqlTransactionManager", 
		basePackages = "com.pocdatasource.repository"
)
public class PostgresqlConfiguration {
 
	/**
	 * PostgreSQL datasource definition.
	 * 
	 * @return datasource.
	 */
	@Primary
	@Bean(name = "postgresqlDataSource")
	@ConfigurationProperties(prefix = "spring.postgresql.datasource")
	public DataSource postgresqlDataSource() {
		return DataSourceBuilder
					.create()
					.build();
	}
 
	/**
	 * Entity manager definition. 
	 *  
	 * @param builder an EntityManagerFactoryBuilder.
	 * @return LocalContainerEntityManagerFactoryBean.
	 */
	@Primary
	@Bean(name = "postgresqlEntityManager")
	public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder, 
			@Qualifier("postgresqlDataSource") DataSource dataSource) {
		return builder
					.dataSource(dataSource)
					.packages("com.pocdatasource.model")
					.persistenceUnit("postgresqlPU")
					.build();
	}
 
	@Primary
	@Bean(name = "postgresqlTransactionManager")
	public PlatformTransactionManager postgresqlTransactionManager(@Qualifier("postgresqlEntityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
 
}

