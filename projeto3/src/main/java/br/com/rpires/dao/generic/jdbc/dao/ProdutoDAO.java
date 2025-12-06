package br.com.rpires.dao.generic.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.rpires.dao.generic.jdbc.ConnectionFactory;
import br.com.rpires.domain.Produto;

public class ProdutoDAO implements IProdutoDAO {

    @Override
    public Integer cadastrar(Produto produto) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        try{
            connection = ConnectionFactory.getConnection();
            String sql = getSqlProductInsert();
            stm = connection.prepareStatement(sql);
            adicionaParametrosInsertProduto(stm, produto);

            return stm.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public Integer atualizar(Produto produto) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlProductUpdate();
            stm = connection.prepareStatement(sql);
            atualizarParametrosProduto(stm, produto);
            return stm.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public Produto buscar(String descricao) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Produto produto = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlProductSearch();
            stm = connection.prepareStatement(sql);
            adicionaParametrosSearchProduct(stm, descricao);
            rs = stm.executeQuery();
            if(rs.next()) {
                produto = new Produto();
                Long id = rs.getLong("ID");
                String descricao_secundaria = rs.getString("DESCRICAO");
                Long preco = rs.getLong("PRECO");
                produto.setId(id);
                produto.setProduct_desc(descricao_secundaria);
                produto.setProduct_price(preco);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, rs);
        }
        return produto;
    }

    @Override
    public List<Produto> buscarTodos() throws Exception {
		Connection connection = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<Produto> list = new ArrayList<>();
		Produto produto = null;

        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlSelectAllProducts();
            // compila os dados
            stm = connection.prepareStatement(sql);
            // roda os dados
            rs = stm.executeQuery();
            //------------------------------------------------------
            while(rs.next()) {
                produto = new Produto();
                Long id = rs.getLong("ID");
                String descricao = rs.getString("DESCRICAO");
                Long preco = rs.getLong("PRECO");
                produto.setId(id);
                produto.setProduct_desc(descricao);
                produto.setProduct_price(preco);
                list.add(produto);
            } 
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, rs);
        }

        return list;
    }

    @Override
    public Integer excluir(Produto produto) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        try{
            connection = ConnectionFactory.getConnection();
            String sql = getSqlProductDeleted();
            stm = connection.prepareStatement(sql);
            adicionaParametrosDeletProduto(stm, produto);

            return stm.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    // SQL CODES

    
    // cadastro de produto
    private String getSqlProductInsert() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_PRODUTO (ID, DESCRICAO, PRECO) ");
        sb.append("VALUES (nextval('sq_produto_id'),?,?)");
        return sb.toString();
    }
    // tradução do cadastro de produto
    private void adicionaParametrosInsertProduto(PreparedStatement stm, Produto produto) throws SQLException {
        stm.setString(1, produto.getProduct_desc());
        stm.setLong(2, produto.getProduct_price());
    }
    
    // atualização de dados
    private String getSqlProductUpdate() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_PRODUTO ");
        sb.append("SET DESCRICAO = ?, PRECO = ? ");
        sb.append("WHERE ID = ?");
        return sb.toString();
    }
    // tradução da atualização
    private void atualizarParametrosProduto(PreparedStatement stm, Produto produto) throws SQLException{
        stm.setString(1, produto.getProduct_desc());
        stm.setLong(2, produto.getProduct_price());
        stm.setLong(3, produto.getId());
    }
    
    // busca de produto
    private String getSqlProductSearch() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM TB_PRODUTO ");
        sb.append("WHERE DESCRICAO = ?");
        return sb.toString();
    }
    // tradução da busca
    private void adicionaParametrosSearchProduct(PreparedStatement stm, String descricao) throws SQLException{
        stm.setString(1, descricao);
    }

    // Busca TODOS
    private String getSqlSelectAllProducts() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM TB_PRODUTO");
        return sb.toString();
    }

    // Excluir da base de dados
    private String getSqlProductDeleted() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM TB_PRODUTO ");
        sb.append("WHERE DESCRICAO = ?");
        return sb.toString();
    }
    // Tradução da Exclusão
    private void adicionaParametrosDeletProduto(PreparedStatement stm, Produto produto) throws SQLException {
        stm.setString(1, produto.getProduct_desc());
    }

















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
