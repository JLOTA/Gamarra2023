package com.Gamarra.app.Service;

import com.Gamarra.app.Dto.*;
import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Persistencia.PedidoRepository;
import com.Gamarra.app.Utils.PedidoInforme;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PedidoService {

    private DtoPedido dtoPedido;

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;
    private final EstadoService estadoService;
    private final ServicioService servicioService;
    private final DetallePedidoService detalleService;

    public void nuevoPedido(String usuario) {
        dtoPedido = new DtoPedido();
        dtoPedido.setCorrelativo(this.generarCorrelativo());
        dtoPedido.setFecha(this.obtenerFecha());
        dtoPedido.setEstado(estadoService.buscarPorId(1));
        dtoPedido.setUsuario(usuarioService.buscarPorUsuario(usuario));
    }

    public Pedido verPedido() {
        Pedido pedido = new Pedido();
        if (dtoPedido.getCliente() == null) {
            pedido.setCliente(new Cliente());
        } else {
            pedido.setCliente(dtoPedido.getCliente());
        }
        if (dtoPedido.getEmpleado() == null) {
            pedido.setEmpleado(new Empleado());
        } else {
            pedido.setEmpleado(dtoPedido.getEmpleado());
        }

        pedido.setEstado(dtoPedido.getEstado());
        pedido.setUsuario(dtoPedido.getUsuario());
        pedido.setFecha(dtoPedido.getFecha());
        pedido.setCorrelativo(dtoPedido.getCorrelativo());
        pedido.setSubtotal(dtoPedido.getSubtotal());
        return pedido;
    }

    public void agregarServicio(int id, double cantidad, String observacion) {
        Servicio servicio = servicioService.buscarServicioPorId(id);
        dtoPedido.agregar(servicio, cantidad, observacion);
    }

    public void quitarServicio(int id) {
        dtoPedido.quitar(id);
    }

    public List<DtoDetallePedido> verCarrito() {
        List<DtoDetallePedido> listaDetalles = new ArrayList<>();

        if (dtoPedido != null) {
            for (DtoDetallePedido dtoDetalle : dtoPedido.getDetalles()) {
                DtoDetallePedido dtoDetalleCarrito = new DtoDetallePedido();
                dtoDetalleCarrito.setServicio(dtoDetalle.getServicio());
                dtoDetalleCarrito.setCantidad(dtoDetalle.getCantidad());
                dtoDetalleCarrito.setObservacion(dtoDetalle.getObservacion());

                dtoDetalleCarrito.setTotalUnitario(dtoDetalle.getTotalUnitario());

                listaDetalles.add(dtoDetalleCarrito);
            }
        }

        return listaDetalles;
    }

    public void asignarCliente(String documentoCliente) {
        dtoPedido.setCliente(clienteService.buscarPorDocumento(documentoCliente));
    }

    public void asignarEmpleado(String dniEmpleado) {
        dtoPedido.setEmpleado(empleadoService.buscarEmpleadoPorDni(dniEmpleado));
    }

    public String grabarPedidoConDetalles() {
        Pedido pedidoGrabado = this.grabarPedido(dtoPedido);
        String msg = "";
        for (int i = 0; i < dtoPedido.getDetalles().size(); i++) {
            DtoDetallePedido dtoDetalle = (DtoDetallePedido) dtoPedido.getDetalles().get(i);
            msg = detalleService.grabarDetalle(dtoDetalle, pedidoGrabado);
        }
        return msg;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido grabarPedido(DtoPedido dto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(dto.getCliente());
        pedido.setEmpleado(dto.getEmpleado());
        pedido.setEstado(dto.getEstado());
        pedido.setUsuario(dto.getUsuario());
        pedido.setFecha(dto.getFecha());
        pedido.setCorrelativo(dto.getCorrelativo());
        pedido.setSubtotal(dto.getSubtotal());
        pedidoRepository.save(pedido);
        return pedido;
    }

    public String eliminarPedido(Pedido pedido) {
        pedidoRepository.delete(pedido);
        return "Pedido eliminado";
    }

    public Map<LocalDate, PedidoInforme> obtenerPedidosDiarios(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pedido> pedidos = pedidoRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<LocalDate, PedidoInforme> pedidosDiarios = new TreeMap<>();
        for (Pedido pedido : pedidos) {
            LocalDate fechaPedido = pedido.getFecha();
            PedidoInforme informe = pedidosDiarios.getOrDefault(fechaPedido, new PedidoInforme());

            informe.incrementarCantidadPedidos();
            informe.sumarGanancias(pedido.getSubtotal());

            pedidosDiarios.put(fechaPedido, informe);
        }

        return pedidosDiarios;
    }

    public Map<LocalDate, PedidoInforme> obtenerPedidosSemanales(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pedido> pedidos = pedidoRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<LocalDate, PedidoInforme> pedidosSemanales = new TreeMap<>();
        for (Pedido pedido : pedidos) {
            LocalDate fechaPedido = pedido.getFecha();
            LocalDate inicioSemana = fechaPedido.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            PedidoInforme informe = pedidosSemanales.getOrDefault(inicioSemana, new PedidoInforme());

            informe.incrementarCantidadPedidos();
            informe.sumarGanancias(pedido.getSubtotal());

            pedidosSemanales.put(inicioSemana, informe);
        }

        return pedidosSemanales;
    }

    public Map<YearMonth, PedidoInforme> obtenerPedidosMensuales(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pedido> pedidos = pedidoRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<YearMonth, PedidoInforme> pedidosMensuales = new TreeMap<>();
        for (Pedido pedido : pedidos) {
            YearMonth yearMonth = YearMonth.from(pedido.getFecha());
            PedidoInforme informe = pedidosMensuales.getOrDefault(yearMonth, new PedidoInforme());

            informe.incrementarCantidadPedidos();
            informe.sumarGanancias(pedido.getSubtotal());

            pedidosMensuales.put(yearMonth, informe);
        }

        return pedidosMensuales;
    }

    public Pedido buscarPorId(int id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    private String generarCorrelativo() {
        int numero = 0;
        String correlativo = "";

        List<Pedido> pedidos = this.listarPedidos();
        List<Integer> numeros = new ArrayList<Integer>();

        pedidos.stream().forEach(p -> numeros.add(p.getIdPedido()));

        if (pedidos.isEmpty()) {
            numero = 1;
        } else {
            numero = numeros.stream().max(Integer::compare).get();
            numero++;
        }

        correlativo = "P-" + String.format("%010d", numero);

        return correlativo;
    }

    private LocalDate obtenerFecha() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaFormateada = fechaActual.format(formatter);
        return LocalDate.parse(fechaFormateada, formatter);
    }

    public Page<Pedido> obtenerPedidosPaginados(Pageable pageable) {
        return pedidoRepository.findAllByOrderByFechaDesc(pageable);
    }
}
