package dbjpa;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import domain.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import util.JpaUtil;

public class App {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int op = -1;
		
		do {
			try {
				System.out.println();
				System.out.println("1 - Cadastrar");
				System.out.println("2 - Listar");
				System.out.println("3 - Buscar por ID");
				System.out.println("4 - Alterar");
				System.out.println("5 - Excluir");
				System.out.println("0 - Sair");
				System.out.print("Escolha: ");
				
				if (scan.hasNextInt()) {
					op = scan.nextInt();
				} else {
					System.out.println("\nErro: entrada inválida!");
					scan.next();
					continue; // reinicia o loop
				}
				
				switch (op) {
				case 1:
					cadastrar();
					break;
				case 2:
					listar();
					break;
				case 3:
					buscarID();
					break;
				case 4:
					alterar();
					break;
				case 5:
					deletar();
					break;
				case 0:
					System.out.println("\nFim do programa!");
					break;
				default:
					System.out.println("Opção inálida!");
					break;
				}
			} catch (Exception e) {
				System.out.println("Erro: " + e);
			}
		} while (op!=0);
		
		scan.close();
		JpaUtil.close();
	}
	
	public static void cadastrar() {
		EntityManager manager = JpaUtil.getEntityManager();
		Scanner entrada = new Scanner(System.in);
		
		try {
			System.out.print("Nome: ");
			String n = entrada.nextLine();
			System.out.print("Salário: ");
			Double s = 0.0;
			if (entrada.hasNextDouble()) {
				s = entrada.nextDouble();
			} else {
				System.out.println("\nErro: entrada inválida!");
				entrada.next();
				return;
			}
			
			EntityTransaction tx = manager.getTransaction();
			tx.begin();
			
			Pessoa p = new Pessoa();
			p.setNome(n);
			p.setSalario(new BigDecimal(s));
			
			manager.persist(p);
			tx.commit();
			
			System.out.println("Pessoa cadastrada com sucesso!");
			
		} catch (Exception e) {
			System.out.println("Erro ao cadastrar: " + e.getMessage());
		} finally {
			manager.close();
		}
	}
	
	public static void listar() {
		EntityManager manager = JpaUtil.getEntityManager();
		
		try {								// usar nome da entidade
			Query query = manager.createQuery("select p from Pessoa p"); // JPQL (Java Persistence Query Language)
			@SuppressWarnings({ "unused", "unchecked" })
			List<Pessoa> pessoas = query.getResultList();
			
			for (Pessoa pessoa : pessoas) {
				System.out.println();
				System.out.println("ID: " + pessoa.getId());
				System.out.println("Nome: " + pessoa.getNome());
				System.out.println("Salário: R$ " + pessoa.getSalario());
			}
		} catch (Exception e) {
			System.out.println("Erro ao listar: " + e.getMessage());
		} finally {
			if (manager.isOpen()) {
				manager.close();
			}
		}
	}
	
	public static void buscarID() {
		EntityManager manager = JpaUtil.getEntityManager();
		Scanner entrada = new Scanner(System.in);
		
		try {
			Long id = 0L;
			System.out.print("Digite o ID a ser buscado: ");
			id = entrada.nextLong();
			
			Pessoa pessoa = manager.find(Pessoa.class, id);
			if (pessoa == null) {
				System.out.println("\nNenhuma pessoa encontrada com esse Id!");
				return;
			}
			System.out.println();
			System.out.println("ID: " + pessoa.getId());
			System.out.println("Nome: " + pessoa.getNome());
			System.out.println("Salário: R$ " + pessoa.getSalario());
			
		} catch (Exception e) {
			System.out.println("Erro ao fazer busca: " + e.getMessage());
		} finally {
			manager.close();
		}
	}
	
	public static void alterar() {
		EntityManager manager = JpaUtil.getEntityManager();
		
		Scanner entrada = new Scanner(System.in);
		try {
			System.out.print("\nDigite o ID da pessoa que deseja editar: ");
			Long id = entrada.nextLong();
			Pessoa pessoa = manager.find(Pessoa.class, id);
			if (pessoa == null) {
				System.out.println("\nNenhuma pessoa encontrada com esse Id!");
				return;
			}
			System.out.println();
			System.out.println("ID: " + pessoa.getId());
			System.out.println("Nome: " + pessoa.getNome());
			System.out.println("Salário: R$ " + pessoa.getSalario());
			
			entrada.nextLine();
			System.out.print("\nNome: ");
			String n = entrada.nextLine();
			System.out.print("Salário: ");
			Double s = 0.0;
			if (entrada.hasNextDouble()) {
				s = entrada.nextDouble();
			} else {
				System.out.println("\nErro: entrada inválida!");
				entrada.next();
				return;
			}
			
			EntityTransaction tx = manager.getTransaction();
			tx.begin();
	
			pessoa.setNome(n);
			pessoa.setSalario(new BigDecimal(s));
			
			tx.commit();
			
			System.out.println(pessoa.getNome() + " alterado com sucesso!");
			
		} catch (Exception e) {
			System.out.println("Erro ao tentar editar: " + e.getMessage());
		} finally {
			manager.close();
		}
	}
	
	
	public static void deletar() {
		EntityManager manager = JpaUtil.getEntityManager();
		Scanner entrada = new Scanner(System.in);
		
		try {
			System.out.print("Digite o ID da pessoa a ser excluída: ");
			Long id = entrada.nextLong();
			
			Pessoa pessoa = manager.find(Pessoa.class, id);
			if (pessoa == null) {
				System.out.println("\nNenhuma pessoa encontrada com esse Id!");
				return;
			}
			EntityTransaction tx = manager.getTransaction();
			tx.begin();
	
			Optional<Pessoa> optionalPessoa = Optional.of(pessoa);
			optionalPessoa.ifPresent(p -> manager.remove(p));
			
			tx.commit();
			System.out.println(pessoa.getNome() + " excluído com sucesso!");
			
		} catch (Exception e) {
			System.out.println("Erro ao tentar excluir: " + e.getMessage());
		} finally {
			manager.close();
		}
	}
	

}

