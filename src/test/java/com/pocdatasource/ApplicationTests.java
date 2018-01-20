package com.pocdatasource;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pocdatasource.model.Author;
import com.pocdatasource.repository.AuthorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
 
	@Autowired
	private AuthorRepository authorRepository;
 
	@PersistenceUnit(unitName = "mysqlPU")
	private EntityManagerFactory emfMySql;
	
	@PersistenceUnit(unitName = "postgresqlPU")
	private EntityManagerFactory emfPostgre;
	
	@After
	public void end() {
		authorRepository.deleteAll();
		
		EntityManager entityManager = emfMySql.createEntityManager();
		
		entityManager.getTransaction().begin();
		entityManager.createQuery("delete from Author").executeUpdate();
		entityManager.getTransaction().commit();
		
		entityManager.close();
	}
	
	@Before
	public void init() {
 
		Author authorMysql1 = new Author();
		authorMysql1.setFirstName("Author");
		authorMysql1.setLastName("Mysql via EntityManager");
 
		Author authorPostgres1 = new Author();
		authorPostgres1.setFirstName("Author");
		authorPostgres1.setLastName("Postgre via EntityManager");
		
		Author authorMysql1AndPostgre1 = new Author();
		authorMysql1AndPostgre1.setFirstName("Author");
		authorMysql1AndPostgre1.setLastName("Postgre JpaRepository Default");
		
		/*
		 * Salvando via EntityManager e DataSource MySql
		 */
		save(emfMySql, authorMysql1);
		
		/*
		 * Salvando via EntityManager e DataSource Postgre
		 */
		save(emfPostgre, authorPostgres1);
		
		/*
		 * Salvando via JpaRepository e Primary DataSource
		 */
		authorRepository.save(authorMysql1AndPostgre1);
	}

	private void save(EntityManagerFactory emf, Author authorPostgres1) {
		
		EntityManager entityManager = emf.createEntityManager();
		
		entityManager.getTransaction().begin();
		entityManager.persist(authorPostgres1);
		entityManager.getTransaction().commit();
		
		entityManager.close();
	}
 
	@Test
	public void testPrimaryDataSource1() {
		
		List<Author> authors = authorRepository.findAll();
		
		Assert.assertTrue(authors.size() == 2);
		
	}
	
	@Test
	public void testPrimaryDataSource2() {
		
		EntityManager entityManager = emfPostgre.createEntityManager();
		
		List<Author> authors = entityManager.createQuery("select a from Author a", Author.class).getResultList();
		
		Assert.assertTrue(authors.size() == 2);
		
		entityManager.close();
		
	}
	
	@Test
	public void testSecundaryDataSource() {
		
		EntityManager entityManager = emfMySql.createEntityManager();
		
		List<Author> authors = entityManager.createQuery("select a from Author a", Author.class).getResultList();
		
		Assert.assertTrue(authors.size() == 1);
		
		entityManager.close();
		
	}
 
}