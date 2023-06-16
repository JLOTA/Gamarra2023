package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Persistencia.*;
import com.Gamarra.app.Utils.Informe;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;
    private final TipoVentaService tipoService;

    private Venta venta;

    @Autowired
    public VentaService(VentaRepository ventaRepository, UsuarioService usuarioService, PedidoService pedidoService, TipoVentaService tipoService) {
        this.ventaRepository = ventaRepository;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
        this.tipoService = tipoService;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public void nuevaVenta(String usuario, Pedido pedido) {
        venta = new Venta();
        venta.setFecha(this.obtenerFecha());
        venta.setCorrelativo(this.generarCorrelativo());
        venta.setPedido(pedido);
        venta.setUsuario(usuarioService.buscarPorUsuario(usuario));
    }

    public String grabarVenta(int idTipo, double descuento) {
        venta.setTipoVenta(tipoService.buscarPorId(idTipo));
        venta.setDescuento(descuento);
        if (venta.getTipoVenta().getIdTipo() == 1) {//boleta
            venta.setTotal(venta.getPedido().getSubtotal() - descuento);
        } else if (venta.getTipoVenta().getIdTipo() == 2) {//factura
            venta.setTotal((venta.getPedido().getSubtotal() - descuento) * 1.18);
        }
        String msg = "";
        if (ventaRepository.save(venta) != null) {
            msg = "Venta grabada: " + venta.getCorrelativo();
            pedidoService.venderPedido(venta.getPedido());
        } else {
            msg = "Error al procesar venta";
        }

        return msg;
    }

    public Venta verVenta() {
        return venta;
    }

    private String generarCorrelativo() {
        int numero = 0;
        String correlativo = "";

        List<Venta> ventas = this.listarVentas();
        List<Integer> numeros = new ArrayList<Integer>();

        ventas.stream().forEach(p -> numeros.add(p.getIdVenta()));

        if (ventas.isEmpty()) {
            numero = 1;
        } else {
            numero = numeros.stream().max(Integer::compare).get();
            numero++;
        }

        correlativo = "V-" + String.format("%010d", numero);

        return correlativo;
    }

    private LocalDate obtenerFecha() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaFormateada = fechaActual.format(formatter);
        return LocalDate.parse(fechaFormateada, formatter);
    }

    public Page<Venta> obtenerVentasPaginados(Pageable pageable) {
        return ventaRepository.findAllByOrderByFechaDesc(pageable);
    }

    public Map<LocalDate, Informe> obtenerVentasDiarias() {
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        LocalDate fechaFin = LocalDate.now();
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<LocalDate, Informe> ventasDiarias = new TreeMap<>();
        for (Venta ven : ventas) {
            LocalDate fechaVenta = ven.getFecha();
            Informe informe = ventasDiarias.getOrDefault(fechaVenta, new Informe());

            Pedido pedido = ven.getPedido();
            List<DetallePedido> detalles = pedido.getDetalles();

            for (DetallePedido detalle : detalles) {
                Servicio servicio = detalle.getServicio();
                String nombreServicio = servicio.getAbreviatura();
                informe.incrementarSolicitudesServicio(nombreServicio);
            }

            informe.incrementarCantidad();
            informe.sumarGanancias(ven.getTotal());

            ventasDiarias.put(fechaVenta, informe);
        }

        return ventasDiarias;
    }

    public Map<LocalDate, Informe> obtenerVentasSemanales() {
        LocalDate fechaInicio = LocalDate.now().minusDays(28).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fechaFin = LocalDate.now();
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<LocalDate, Informe> ventasSemanales = new TreeMap<>();
        for (Venta ven : ventas) {
            LocalDate fechaPedido = ven.getFecha();
            LocalDate inicioSemana = fechaPedido.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            Informe informe = ventasSemanales.getOrDefault(inicioSemana, new Informe());

            Pedido pedido = ven.getPedido();
            List<DetallePedido> detalles = pedido.getDetalles();

            for (DetallePedido detalle : detalles) {
                Servicio servicio = detalle.getServicio();
                String nombreServicio = servicio.getAbreviatura();
                informe.incrementarSolicitudesServicio(nombreServicio);
            }

            informe.incrementarCantidad();
            informe.sumarGanancias(ven.getTotal());

            ventasSemanales.put(inicioSemana, informe);
        }

        return ventasSemanales;
    }

    public Map<YearMonth, Informe> obtenerVentasMensuales() {
        LocalDate fechaInicio = LocalDate.now().minusDays(140).withDayOfMonth(1);
        LocalDate fechaFin = LocalDate.now();
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<YearMonth, Informe> ventasMensuales = new TreeMap<>();
        for (Venta ven : ventas) {
            YearMonth yearMonth = YearMonth.from(ven.getFecha());
            Informe informe = ventasMensuales.getOrDefault(yearMonth, new Informe());

            Pedido pedido = ven.getPedido();
            List<DetallePedido> detalles = pedido.getDetalles();

            for (DetallePedido detalle : detalles) {
                Servicio servicio = detalle.getServicio();
                String nombreServicio = servicio.getAbreviatura();
                informe.incrementarSolicitudesServicio(nombreServicio);
            }

            informe.incrementarCantidad();
            informe.sumarGanancias(ven.getTotal());

            ventasMensuales.put(yearMonth, informe);
        }

        return ventasMensuales;
    }
}
