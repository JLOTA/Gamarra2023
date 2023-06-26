package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Persistencia.*;
import com.Gamarra.app.Utils.Informe;
import com.Gamarra.app.Utils.JsonUtils;
import com.Gamarra.app.Utils.RedondeoUtil;
import com.Gamarra.app.Utils.ReportesUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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

    public Venta buscarPorId(int id) {
        return ventaRepository.findById(id).orElse(null);
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
        double total = 0.00;
        if (venta.getTipoVenta().getIdTipo() == 1) {//boleta
            total = venta.getPedido().getSubtotal() - descuento;
        } else if (venta.getTipoVenta().getIdTipo() == 2) {//factura
            total = (venta.getPedido().getSubtotal() - descuento) * 1.18;
        }
        venta.setTotal(RedondeoUtil.redondear(total));
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

    public Page<Venta> buscarPorCorrelativo(Pageable pageable, String correlativo) {
        return ventaRepository.findAllByCorrelativoContainingOrderByFechaDesc(correlativo, pageable);
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

    public String obtenerServiciosMasVendidosDelMes() { // de los ultimos 30 dias
        LocalDate fechaInicio = LocalDate.now().minusDays(30);
        LocalDate fechaFin = LocalDate.now();
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<String, Double> serviciosMasVendidos = new TreeMap<>();

        for (Venta ven : ventas) {
            for (DetallePedido detalle : ven.getPedido().getDetalles()) {
                Servicio servicio = detalle.getServicio();
                String nombreServicio = servicio.getAbreviatura();
                Double cantidadServicio = serviciosMasVendidos.getOrDefault(nombreServicio, 0.0);
                cantidadServicio += detalle.getCantidad();
                serviciosMasVendidos.put(nombreServicio, cantidadServicio);
            }
        }
        return JsonUtils.toJson(serviciosMasVendidos);
    }

    public List<Venta> obtenerVentasPorMes(String monthYear) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(monthYear, formatter);
        return ventaRepository.findByYearAndMonth(yearMonth.getYear(), yearMonth.getMonth().getValue());
    }

    public String obtenerPeriodo(String fecha) {
        LocalDate localDate = LocalDate.parse(fecha + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nombreMes = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anio = localDate.getYear();
        String resultado = nombreMes + " " + anio;
        return resultado;
    }

    public ReportesUtils reporteVentasDetalles(List<Venta> ventas) {
        int cantidad = 0;
        cantidad = ventas.size();
        double gananciaTotal = 0;
        double gananciaPromedio = 0;
        for (Venta venta : ventas) {
            gananciaTotal += venta.getTotal();
        }
        gananciaPromedio = gananciaTotal / cantidad;
        ReportesUtils detalles = new ReportesUtils(ventas, this.ventasToJson(ventas), cantidad, RedondeoUtil.redondear(gananciaTotal), RedondeoUtil.redondear(gananciaPromedio));
        return detalles;
    }

    public List<ReportesUtils> reporteEmpleadoDetalles(List<Venta> ventas) {
        Map<Empleado, Integer> cantidadPorEmpleado = new HashMap<>();
        Map<Empleado, Double> gananciaTotalPorEmpleado = new HashMap<>();

        for (Venta venta : ventas) {
            Empleado empleado = venta.getPedido().getEmpleado();
            cantidadPorEmpleado.put(empleado, cantidadPorEmpleado.getOrDefault(empleado, 0) + 1);
            gananciaTotalPorEmpleado.put(empleado, gananciaTotalPorEmpleado.getOrDefault(empleado, 0.0) + venta.getTotal());
        }

        List<ReportesUtils> listaEmpleados = new ArrayList<>();

        for (Map.Entry<Empleado, Integer> entry : cantidadPorEmpleado.entrySet()) {
            Empleado empleado = entry.getKey();
            int cantidad = entry.getValue();
            double gananciaTotal = gananciaTotalPorEmpleado.get(empleado);
            double gananciaPromedio = gananciaTotal / cantidad;
            ReportesUtils detalles = new ReportesUtils(empleado, empleado.toString(), cantidad, RedondeoUtil.redondear(gananciaTotal), RedondeoUtil.redondear(gananciaPromedio));
            listaEmpleados.add(detalles);
        }

        Collections.sort(listaEmpleados, new Comparator<ReportesUtils>() {
            @Override
            public int compare(ReportesUtils r1, ReportesUtils r2) {
                return Integer.compare(r2.getCantidad(), r1.getCantidad());
            }
        });

        return listaEmpleados;
    }

    public List<ReportesUtils> reporteClienteDetalles(List<Venta> ventas) {
        Map<Cliente, Integer> cantidadPorCliente = new HashMap<>();
        Map<Cliente, Double> gananciaTotalPorCliente = new HashMap<>();

        for (Venta venta : ventas) {
            Cliente cliente = venta.getPedido().getCliente();
            cantidadPorCliente.put(cliente, cantidadPorCliente.getOrDefault(cliente, 0) + 1);
            gananciaTotalPorCliente.put(cliente, gananciaTotalPorCliente.getOrDefault(cliente, 0.0) + venta.getTotal());
        }

        List<ReportesUtils> listaClientes = new ArrayList<>();

        for (Map.Entry<Cliente, Integer> entry : cantidadPorCliente.entrySet()) {
            Cliente cliente = entry.getKey();
            int cantidad = entry.getValue();
            double gananciaTotal = gananciaTotalPorCliente.get(cliente);
            double gananciaPromedio = gananciaTotal / cantidad;
            ReportesUtils detalles = new ReportesUtils(cliente, cliente.toString(), cantidad, RedondeoUtil.redondear(gananciaTotal), RedondeoUtil.redondear(gananciaPromedio));
            listaClientes.add(detalles);
        }

        Collections.sort(listaClientes, new Comparator<ReportesUtils>() {
            @Override
            public int compare(ReportesUtils r1, ReportesUtils r2) {
                return Integer.compare(r2.getCantidad(), r1.getCantidad());
            }
        });

        return listaClientes;
    }

    public List<ReportesUtils> reporteServicioDetalles(List<Venta> ventas) {
        Map<Servicio, Integer> cantidadPorServicio = new HashMap<>();
        Map<Servicio, Double> gananciaTotalPorServicio = new HashMap<>();

        for (Venta venta : ventas) {
            for (DetallePedido detalles : venta.getPedido().getDetalles()) {
                Servicio servicio = detalles.getServicio();
                cantidadPorServicio.put(servicio, cantidadPorServicio.getOrDefault(servicio, 0) + 1);
                gananciaTotalPorServicio.put(servicio, gananciaTotalPorServicio.getOrDefault(servicio, 0.0) + detalles.getTotalUnitario());
            }
        }

        List<ReportesUtils> listaServicios = new ArrayList<>();

        for (Map.Entry<Servicio, Integer> entry : cantidadPorServicio.entrySet()) {
            Servicio servicio = entry.getKey();
            int cantidad = entry.getValue();
            double gananciaTotalServicio = gananciaTotalPorServicio.get(servicio);
            double gananciaPromedio = gananciaTotalServicio / cantidad;
            ReportesUtils detalles = new ReportesUtils(servicio, servicio.toString(), cantidad, RedondeoUtil.redondear(gananciaTotalServicio), RedondeoUtil.redondear(gananciaPromedio));
            listaServicios.add(detalles);
        }

        Collections.sort(listaServicios, new Comparator<ReportesUtils>() {
            @Override
            public int compare(ReportesUtils r1, ReportesUtils r2) {
                return Integer.compare(r2.getCantidad(), r1.getCantidad());
            }
        });

        return listaServicios;
    }

    public String ventasToJson(List<Venta> ventas) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        try {
            for (int i = 0; i < ventas.size(); i++) {
                Venta venta = ventas.get(i);
                jsonBuilder.append(venta.toString());

                if (i < ventas.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
    
    public String reportesToJson(List<ReportesUtils> reportes) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        try {
            for (int i = 0; i < reportes.size(); i++) {
                ReportesUtils ru = reportes.get(i);
                jsonBuilder.append(ru.toString());

                if (i < reportes.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

}
