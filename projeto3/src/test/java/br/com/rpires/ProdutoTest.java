package br.com.rpires;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import br.com.rpires.dao.generic.jdbc.dao.IProdutoDAO;
import br.com.rpires.dao.generic.jdbc.dao.ProdutoDAO;
import br.com.rpires.domain.Produto;

public class ProdutoTest {

    private IProdutoDAO produtoDAO;

    @Test
    public void cadastroProdutoTeste() throws Exception {
        produtoDAO = new ProdutoDAO();

        Produto produto = new Produto();
        produto.setProduct_desc("Barbeador");
        produto.setProduct_price(8900l);
        Integer countCad = produtoDAO.cadastrar(produto);
        assertTrue(countCad == 1);

        Produto produtoDB = produtoDAO.buscar("Barbeador");
        assertNotNull(produtoDB);
		assertEquals(produto.getProduct_desc(), produtoDB.getProduct_desc());
		assertEquals(produto.getProduct_price(), produtoDB.getProduct_price());

        Integer countDel = produtoDAO.excluir(produtoDB);
        assertTrue(countDel == 1);
    }

    @Test
	public void buscarProdutoTest() throws Exception {
		produtoDAO = new ProdutoDAO();
		
		Produto produto = new Produto();
		produto.setProduct_desc("Escova de Dente");
		produto.setProduct_price(1750l);
		Integer countCad = produtoDAO.cadastrar(produto);
		assertTrue(countCad == 1);
		
		Produto produtoBD = produtoDAO.buscar("Escova de Dente");
		assertNotNull(produtoBD);
		assertEquals(produto.getProduct_desc(), produtoBD.getProduct_desc());
		assertEquals(produto.getProduct_price(), produtoBD.getProduct_price());
		
		Integer countDel = produtoDAO.excluir(produtoBD);
		assertTrue(countDel == 1);
	}
	
	@Test
	public void excluirProdutoTest() throws Exception {
		produtoDAO = new ProdutoDAO();
		
		Produto produto = new Produto();
		produto.setProduct_desc("mesinha pra pizza");
		produto.setProduct_price(600l);
		Integer countCad = produtoDAO.cadastrar(produto);
		assertTrue(countCad == 1);
		
		Produto produtoBD = produtoDAO.buscar("mesinha pra pizza");
		assertNotNull(produtoBD);
		assertEquals(produto.getProduct_desc(), produtoBD.getProduct_desc());
		assertEquals(produto.getProduct_price(), produtoBD.getProduct_price());
		
		Integer countDel = produtoDAO.excluir(produtoBD);
		assertTrue(countDel == 1);
	}
	
	@Test
	public void buscarTodosTest() throws Exception {
		produtoDAO = new ProdutoDAO();		
		Produto produto = new Produto();
		produto.setProduct_desc("fone de ouvido");
		produto.setProduct_price(2500l);
		Integer countCad = produtoDAO.cadastrar(produto);
		assertTrue(countCad == 1);
		Produto produtos = new Produto();
		produtos.setProduct_desc("fone de ouvido sem fio");
		produtos.setProduct_price(3500l);
		Integer countCad2 = produtoDAO.cadastrar(produtos);
		assertTrue(countCad2 == 1);
		List<Produto> list = produtoDAO.buscarTodos();
		assertNotNull(list);
		assertEquals(2, list.size());
		int countDel = 0;
		for (Produto pro : list) {
			produtoDAO.excluir(pro);
			countDel++;
		}
		assertEquals(list.size(), countDel);
		list = produtoDAO.buscarTodos();
		assertEquals(list.size(), 0);
	}
	
	@Test
	public void atualizarTest() throws Exception {
		produtoDAO = new ProdutoDAO();
		
		Produto produto = new Produto();
		produto.setProduct_desc("Rolo de massa");
		produto.setProduct_price(7900l);
		Integer countCad = produtoDAO.cadastrar(produto);
		assertTrue(countCad == 1);
		
		Produto produtoBD = produtoDAO.buscar("Rolo de massa");
		assertNotNull(produtoBD);
		assertEquals(produto.getProduct_desc(), produtoBD.getProduct_desc());
		assertEquals(produto.getProduct_price(), produtoBD.getProduct_price());
		
		produtoBD.setProduct_desc("Ventilador");
		produtoBD.setProduct_price(29990l);
		Integer countUpdate = produtoDAO.atualizar(produtoBD);
		assertTrue(countUpdate == 1);
		
		Produto produtoBD1 = produtoDAO.buscar("Rolo de massa");
		assertNull(produtoBD1);
		
		Produto produtoBD2 = produtoDAO.buscar("Ventilador");
		assertNotNull(produtoBD2);
		assertEquals(produtoBD.getId(), produtoBD2.getId());
		assertEquals(produtoBD.getProduct_desc(), produtoBD2.getProduct_desc());
		assertEquals(produtoBD.getProduct_price(), produtoBD2.getProduct_price());
		
		List<Produto> list = produtoDAO.buscarTodos();
		for (Produto pro : list) {
			produtoDAO.excluir(pro);
		}
	}
}
