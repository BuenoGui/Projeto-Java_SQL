/**
 * 
 */
package br.com.rpires.dao.generic.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.rpires.dao.generic.jdbc.ConnectionFactory;
import br.com.rpires.domain.Cliente;

/**
 * @author rodrigo.pires
 *
 */
public class ClienteDAO implements IClienteDAO {

	@Override
	public Integer cadastrar(Cliente cliente) throws Exception {
		Connection connection = null;
		// prepareStatment vai servir para checar se o comando SQL está correto
		// então usamos o "getSqlInsert" para criar uma string com os comandos SQL
		// usamos o "prepareStatment" para compilar na variavel "stm"
		PreparedStatement stm = null;
		try {
			connection = ConnectionFactory.getConnection();
			String sql = getSqlInsert();
			stm = connection.prepareStatement(sql);
			adicionarParametrosInsert(stm, cliente);
			// executeUpdate, bom, ele atualiza os valores no banco de dados, 
			// e passando nosso stml, ele os adiciona ao banco
			return stm.executeUpdate();
		} catch(Exception e) {
			throw e;
		} finally {
			// sempre fechar a conexão com o banco de dados
			// após a consulta/inserção de algum valor
			// Boa Pratica
			closeConnection(connection, stm, null);
		}
	}

	// Estrutura é a mesma que o de cadastro
	// Utiliza a mesma logica em todas as partes

	@Override
	public Integer atualizar(Cliente cliente) throws Exception {
		Connection connection = null;
		PreparedStatement stm = null;
			try {
				connection = ConnectionFactory.getConnection();
				String sql = getSqlUpdate();
				stm = connection.prepareStatement(sql);
				adicionarParametrosUpdate(stm, cliente);
				return stm.executeUpdate();
			} catch(Exception e) {
				throw e;
			} finally {
				closeConnection(connection, stm, null);
			}
		}

	// Aqui é uma consulta no banco de dados
	// A estrutura do código mudou um pouco 
	// mas tá seguindo o mesmo padrão dos ultimos
	// Consulta em SQL sendo o "select * from x"

	@Override
	public Cliente buscar(String codigo) throws Exception {
		Connection connection = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		Cliente cliente = null;
			try {
				connection = ConnectionFactory.getConnection();
				String sql = getSqlSelect();
				stm = connection.prepareStatement(sql);
				adicionarParametrosSelect(stm, codigo);
				// o ResultSet é igual a execução do SQL
				// pois ele é o OBJ em Java que vai armazenar os valores das pesquisas em SQL
				rs = stm.executeQuery();
			if (rs.next()) {
					// istancia um cliente 
					cliente = new Cliente();
					// cria as variaveis e passa em qual coluna
					// e qual o tipo do valor dentro da coluna
					// EX "Long id" "rs.getLong("ID"), aqui diz:
					// O valor que vai entrar "id" sendo do tipo Long
					// e após o getLong, o nome da coluna com o tipo
					Long id = rs.getLong("ID");
					String nome = rs.getString("NOME");
					String cd = rs.getString("CODIGO");
					// adiciona valores as variaveis
					cliente.setId(id);
					cliente.setNome(nome);
					cliente.setCodigo(cd);
				}
			} catch(Exception e) {
				throw e;
			} finally {
				closeConnection(connection, stm, rs);
			}
			return cliente;
	}

	// Exclui um item do banco de dados com base no seu código

	@Override
	public Integer excluir(Cliente cliente) throws Exception {
		Connection connection = null;
		PreparedStatement stm = null;
			try {
				connection = ConnectionFactory.getConnection();
				String sql = getSqlDelete();
				stm = connection.prepareStatement(sql);
				adicionarParametrosDelete(stm, cliente);
				return stm.executeUpdate();
			} catch(Exception e) {
				throw e;
			} finally {
				closeConnection(connection, stm, null);
			}
		}
	
	// Busca todos os clientes na tabela
	// Retornando em uma List de <clientes>
	// Lista de OBJETOS, onde todos os OBJ são Clientes

	@Override
		public List<Cliente> buscarTodos() throws Exception {
		Connection connection = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<Cliente> list = new ArrayList<>();
		Cliente cliente = null;
			try {
				connection = ConnectionFactory.getConnection();
				String sql = getSqlSelectAll();
				stm = connection.prepareStatement(sql);
				rs = stm.executeQuery();
				// enquanto o "next" retornar "TRUE",
				// significa que tem mais algum valor dentro do rs
				// e enquanto tiver valores, ele vai ir adicionando 
				// o cliente na lista da busca 
				while (rs.next()) {
					cliente = new Cliente();
					Long id = rs.getLong("ID");
					String nome = rs.getString("NOME");
					String cd = rs.getString("CODIGO");
					cliente.setId(id);
					cliente.setNome(nome);
					cliente.setCodigo(cd);
					list.add(cliente);
				}
			} catch(Exception e) {
				throw e;
			} finally {
				closeConnection(connection, stm, rs);
			}
			return list;
		}
	

	// SQL CODES


	// Aqui é a criação de um cliente no PostgreSql
	// da para ver o comando abaixo criando um valor na tabela cliente
	// Passando Id, Código e nome
	// com nexval para contador
	// e código e contador se encontram como "?"
	private String getSqlInsert() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO TB_CLIENTE (ID, CODIGO, NOME) ");
		sb.append("VALUES (nextval('sq_cliente_id'),?,?)");
		return sb.toString();
	}
	
	// ele recebe o código compilado pelo "PreparedStatement" e adiciona os valores
	// Código e Nome do cliente recebido pelo metodo "cadastrar"
	// utilizando o "setString" conseguimos mudar os valores em "?"
	// começamos os indexes pelo 1, representando a primeira interogação 

	private void adicionarParametrosInsert(PreparedStatement stm, Cliente cliente) throws SQLException {
		stm.setString(1, cliente.getCodigo());
		stm.setString(2, cliente.getNome());
	}
	

	// CÓDIGO EM SQL PARA ATUALIZAR OS VALORES NA TABELA
	// UTILIZADO PARA A FUNÇÃO "ATUALIZAR"
	private String getSqlUpdate() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE TB_CLIENTE ");
		sb.append("SET NOME = ?, CODIGO = ? ");
		sb.append("WHERE ID = ?");
		return sb.toString();
	}
	
	// Recebe o Statment e o cliente
	// E faz o update a cima
	private void adicionarParametrosUpdate(PreparedStatement stm, Cliente cliente) throws SQLException {
		stm.setString(1, cliente.getNome());
		stm.setString(2, cliente.getCodigo());
		stm.setLong(3, cliente.getId());
	}
	
	// Código SQL para remoção de um valor
	// da tabela Tb_cliente


	private String getSqlDelete() {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM TB_CLIENTE ");
		sb.append("WHERE CODIGO = ?");
		return sb.toString();
	}
	
	// coloca o código do cliente para fazer a remoção

	private void adicionarParametrosDelete(PreparedStatement stm, Cliente cliente) throws SQLException {
		stm.setString(1, cliente.getCodigo());
	}
	
	// Aqui é o comando SQL para:
	// fazer a busca do cliente na tabela
	// com base no código do cliente

	private String getSqlSelect() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM TB_CLIENTE ");
		sb.append("WHERE CODIGO = ?");
		return sb.toString();
	}
	
	// adiciona o "código" no código SQL da pesquisa
	private void adicionarParametrosSelect(PreparedStatement stm, String codigo) throws SQLException {
		stm.setString(1, codigo);
	}
	
	private String getSqlSelectAll() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM TB_CLIENTE");
		return sb.toString();
	}
	

	// Aqui ele checa todas as conexões passadas
	// Caso tenha alguma em aberto ele a fecha 
	// Utilizando o "isClosed()" do proprio Java
	private void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (stm != null && !stm.isClosed()) {
				stm.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e1) {
			// pega o erro caso aconteça algum
			e1.printStackTrace();
		}
	}
}
