package br.com.kardec.loja.testes;

import br.com.kardec.loja.dao.CategoriaDao;
import br.com.kardec.loja.dao.ClienteDao;
import br.com.kardec.loja.dao.PedidoDao;
import br.com.kardec.loja.dao.ProdudoDao;
import br.com.kardec.loja.modelo.*;
import br.com.kardec.loja.util.JPAUtil;
import br.com.kardec.loja.vo.RelatorioDeVendasVo;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class CadastroDePedido {

    public static void main(String[] args){
        popularBancoDeDados();
        EntityManager em = JPAUtil.getEntityManager();
        ProdudoDao produdoDao = new ProdudoDao(em);
        ClienteDao clienteDao = new ClienteDao(em);

        Produto produto = produdoDao.buscarPorId(1l);
        Produto produto2 = produdoDao.buscarPorId(2l);
        Produto produto3 = produdoDao.buscarPorId(3l);
        Cliente cliente = clienteDao.buscarPorId(1l);

        em.getTransaction().begin();


        Pedido pedido = new Pedido(cliente);
        pedido.adicionarItem(new ItemPedido(10, pedido, produto));
        pedido.adicionarItem(new ItemPedido(40, pedido, produto2));

        Pedido pedido2 = new Pedido(cliente);
        pedido.adicionarItem(new ItemPedido(2, pedido, produto3));

        PedidoDao pedidoDao = new PedidoDao(em);
        pedidoDao.cadastrar(pedido);
        pedidoDao.cadastrar(pedido2);

        em.getTransaction().commit();


        BigDecimal totalVendido = pedidoDao.valorTotalVendido();
        System.out.println("VALOR TOTAL: " + totalVendido);

        List<RelatorioDeVendasVo> relatorio = pedidoDao.relatorioDeVendas();
        relatorio.forEach(System.out::println);

    }

    private static void popularBancoDeDados() {
        Categoria celulares = new Categoria("CELULARES");
        Categoria videogames = new Categoria("VIDEOGAMES");
        Categoria informatica = new Categoria("INFORMATICA");

        Produto celular = new Produto("Xiami Redmi", "Barato e bom", new BigDecimal("800"), celulares);
        Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("3500"), videogames);
        Produto macbook = new Produto("Macbook", "Macbook para retirada", new BigDecimal("16000"), informatica);

        Cliente cliente = new Cliente("Kardec", "987654");

        EntityManager em = JPAUtil.getEntityManager();
        ProdudoDao produtoDao = new ProdudoDao(em);
        CategoriaDao categoriaDao = new CategoriaDao(em);
        ClienteDao clienteDao = new ClienteDao(em);

        em.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        categoriaDao.cadastrar(videogames);
        categoriaDao.cadastrar(informatica);

        produtoDao.cadastrar(celular);
        produtoDao.cadastrar(videogame);
        produtoDao.cadastrar(macbook);
        clienteDao.cadastrar(cliente);

        em.getTransaction().commit();
        em.close();
    }
}
