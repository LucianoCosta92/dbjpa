package dbjpa;


import java.math.BigDecimal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import util.JpaUtil;

public class App {

	public static void main(String[] args) {
		// gerencia o ciclo de vida das entidades
		EntityManager manager = JpaUtil.getEntityManager();
		// cria uma nova transação
		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		// instanciamos um objeto de Pessoa
		Pessoa p1 = new Pessoa();
		p1.setNome("Maria Lucia");
		p1.setSalario(new BigDecimal(5000));
		
		try {
			// persistindo objeto no banco de dados
			manager.persist(p1);
			// efetiva a inserção 
			tx.commit();
			System.out.println("Sucesso!");
		} catch (Exception e) {
			System.out.println("Erro: " + e);
		}

		manager.close();
		JpaUtil.close();
	}

}

