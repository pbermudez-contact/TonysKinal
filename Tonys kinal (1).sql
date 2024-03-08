Drop database if exists DBTonysKinal2022015;
Create database DBTonysKinal2022015;

Use DBTonysKinal2022015;

Create Table Empresas(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8),
    primary key PK_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key Pk_codigoTipoPlato (codigoTipoPlato)
);

create table Productos(
	codigoProducto int auto_increment not null,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(150) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);


Create table Presupuestos(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal (10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuestos_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
	
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidadPlatos int not null,
    nombrePlato varchar(150) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto)
		references Productos(codigoProducto),
    constraint FK_Productos_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
    constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Empleado(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado)
);
-- ------------------------------------------------------ Procedures De la Base De datos ----------------------------------------------------------------------------------------------------
-- ------------------------------------------------------ Agregar Empresa -------------------------------------------------------------------------------------------------------------------

Delimiter $$
	create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(8))
		begin 
			insert into Empresas (codigoEmpresa, nombreEmpresa, direccion, telefono)
				values(codigoEmpresa, nombreEmpresa, direccion, telefono);
        END$$        
Delimiter ; 

 call sp_AgregarEmpresa('Max','18 av Zona5', '22448899');
 call sp_AgregarEmpresa('Max','17 av Zona6', '12345646');
 call sp_AgregarEmpresa('Max','19 av Zona7', '56435464');
 call sp_AgregarEmpresa('Max','20 av Zona8', '22489419');
 call sp_AgregarEmpresa('Max','21 av Zona9', '22315464');
 
-- select * from Empresas;

-- ---------------------------------------------------- Editar Empresa ---------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarEmpresa(in codEmpresa int,in nomPress varchar(150),in dirr varchar(150), in tel varchar(8))
		begin
			update Empresas set nombreEmpresa = nomPress, direccion = dirr, telefono = tel
				where codigoEmpresa = codEmpresa;
		END$$
Delimiter ;

-- call sp_EditarEmpresa(1,'ElDuende', 'zona2', '22558899');

-- ---------------------------------------------------- Eliminar Empresa ------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarEmpresa(in codEmpresa int)
    begin
		delete from Empresas where codigoEmpresa = codEmpresa;
    END$$
Delimiter ;

-- call sp_EliminarEmpresa(1);
-- ---------------------------------------------------- Listar Empresas -------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarEmpresas()
		begin
			select 
            codigoEmpresa, 
            nombreEmpresa, 
            direccion, 
            telefono
				from Empresas;
        End$$
Delimiter ;

call sp_ListarEmpresas();
-- ---------------------------------------------------- Buscar Empresa --------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarEmpresa(in codEmpresa int)
		Begin
			select 
				codigoEmpresa,
                nombreEmpresa,
                direccion,
                telefono
                from Empresas where codigoEmpresa = codEmpresa;
        End$$
Delimiter ;

-- call sp_BuscarEmpresa(1); 
--
--
-- --------------------------------------------------- Agregar TipoEmpleado ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarTipoEmpleado(in descripcion varchar(150))
		Begin
			Insert into TipoEmpleado (descripcion)
				values (descripcion);
        End$$
Delimiter ;

call sp_AgregarTipoEmpleado('Contador');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Repartidor');
call sp_AgregarTipoEmpleado('Bodega');

-- --------------------------------------------------- Editar TipoEmpleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, in descri varchar (150))
		Begin
        update TipoEmpleado set descripcion = descri
				where codigoTipoEmpleado = codTipoEmpleado;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar TipoEmpleado ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
		Begin
        delete from TipoEmpleado where codigoTipoEmpleado = codTipoEmpleado ;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar TipoEmpleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarTipoEmpleado()
		Begin
			select 
				codigoTipoEmpleado,
                descripcion
            from TipoEmpleado ;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar TipoEmpleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int)
		Begin
        select 
			codigoTipoEmpleado,
			descripcion
			from TipoEmpleado where codigoTipoEmpleado = codTipoEmpleado;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar TipoPlato  ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(150))
		Begin
			Insert into TipoPlato (descripcionTipo)
				values (descripcionTipo);
        End$$
Delimiter ;

call sp_AgregarTipoPlato('Pizza');
call sp_AgregarTipoPlato('Papas');
call sp_AgregarTipoPlato('Jugos');
call sp_AgregarTipoPlato('Lata');
call sp_AgregarTipoPlato('Dona');
-- --------------------------------------------------- Editar TipoPlato -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarTipoPlato(in codTipoPlato int,in des varchar(150))
		Begin
        update TipoPlato set descripcionTipo = des
				where codigoTipoPlato = codTipoPlato;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar TipoPlato ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarTipoPlato(in codTipoPlato int)
		Begin
        delete from TipoPlato where codigoTipoPlato = codTipoPlato; 
        End$$
Delimiter ;
-- --------------------------------------------------- Listar TipoPlato -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarTipoPlato()
		Begin
			select 
				codigoTipoplato,
                descripcionTipo
            from  TipoPlato;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar TipoPlato -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarTipoPlato(in codTipoPlato int)
		Begin
        select 
				codigoTipoTplato,
                descripcionTipo
                from TipoPlato where codigoTipoTplato = codTipoPlato;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Productos ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarProducto(in nombreProducto varchar(150),in cantidad int)
		Begin
			Insert into Productos(nombreProducto,cantidad) 
				values (nombreProducto,cantidad); 
        End$$
Delimiter ;
call sp_AgregarProducto('Hamburguesa',2);
call sp_AgregarProducto('Dona',2);
call sp_AgregarProducto('Pizza',2);
call sp_AgregarProducto('Freez',2);
call sp_AgregarProducto('Malteada',2);
-- --------------------------------------------------- Editar Productos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarProducto(in codProducto int, in nomProducto varchar(150) ,in can int)
		Begin
        update Productos set nombreProducto = nomProducto,cantidad = can
				where codigoProducto = codProducto;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Productos ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarProducto()
		Begin
        delete from Productos where  codigoProducto = codProducto;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Productos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarProductos ()
		Begin
			select 
				codigoProducto,
                nombreProducto,
                cantidad
            from Productos;
            
        End$$
Delimiter ;
call sp_ListarProductos();
-- --------------------------------------------------- Buscar Productos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarProducto()
		Begin
        select 
				codigoProducto,
                nombreProducto,
                cantidad
				from Productos where codigoProducto = codProducto;
        
        End$$
Delimiter ;
--
-- 
-- --------------------------------------------------- Agregar Empleados ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarEmpleado(in numeroEmpleado int,in apellidosEmpleado varchar(150),
    in nombresEmpleado varchar(150), in direccionEmpleado varchar(150),in telefonoContacto varchar(150),in gradoCocinero varchar(150),in codigoTipoEmpleado int)
		Begin
			Insert into Empleados (numeroEmpleado,apellidosEmpleado,nombresEmpleado,direccionEmpleado,telefonoContacto,
		gradoCocinero,codigoTipoEmpleado)
				values (numeroEmpleado,apellidosEmpleado,nombresEmpleado,direccionEmpleado,telefonoContacto,
		gradoCocinero,codigoTipoEmpleado);
        End$$
Delimiter ;

call sp_AgregarEmpleado(1, 'Marcos', 'Bermudez', 'zona 10', '12345678', '1', '1');
call sp_AgregarEmpleado(2, 'Erick', 'Bermudez', 'zona 14', '87456513', '1', '1');
-- --------------------------------------------------- Editar Empleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarEmpleado(in codEmpleado int, in numEmpleado int,in apeEmpleado varchar(150),
    in nomEmpleado varchar(150),in telContacto varchar(150),in graCocinero varchar(150),in codTipoEmpleado int)
		Begin
        update Empleados set numeroEmpleado = numEmpleado , apellidosEmpleado = apeEmpleado,nombresEmpleado = nomEmpleado,telefonoContacto = telContacto,
		 gradoCocinero = graCocinero,codigoTipoEmpleado = codTipoEmpleado
				where codigoEmpleado = codEmpleado;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Empleado ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarEmpleado()
		Begin
        delete from Empleados where codigoEmpleado = codEmpleado;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Empleados -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarEmpleados()
		Begin
			select 
            codigoEmpleado, 
            numeroEmpleado, 
            apellidosEmpleado, 
            nombresEmpleado, 
            telefonoContacto, 
            gradoCocinero, 
            codigoTipoEmpleado
            from  Empleados;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar Empleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarEmpleado()
		Begin
        select 	
			codigoEmpleado, 
			numeroEmpleado, 
			apellidosEmpleado, 
			nombresEmpleado, 
			telefonoContacto, 
			gradoCocinero, 
			codigoTipoEmpleado
			from Empleados where codigoEmpleado = codEmpleado;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Servicio ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarServicio(in fechaServicio date,in tipoServicio varchar(150),in horaServicio time,in lugarServicio varchar(150),
    in telefonoContacto varchar(8),in codigoEmpresa int)
		Begin
			Insert into Servicios( fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End$$
Delimiter ;
call sp_AgregarServicio('2021-03-20', 'Cumple', '11:00', 'zona 29', '12345678', 1);
call sp_AgregarServicio('2022-05-19', 'Fiesta', '15:00', 'zona 24', '87654321', 2);
-- --------------------------------------------------- Editar Servicio -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarServicio(in fecServicio date,in TipServicio varchar(150),in horServicio time,in LugServicio varchar(150),in TelServicio varchar(8), in codEmpresa int,in codServicio int )
		Begin
        update Servicios set fechaServicio = fecServicio, tipoServicio = TipServicio, horaServicio = horServicio, lugarServicio = LugServicio, telefonoContacto = TelServicio, codigoEmpresa = codEmpresa
				where codigoServicio = codServicio;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Servicio ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarServicio()
		Begin
        delete from Servicios where  codigoServicio = codServicio;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Servicios -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarServicios()
		Begin
			select 
            codigoServicio, 
            fechaServicio,
            tipoServicio, 
            horaServicio, 
            lugarServicio, 
            telefonoContacto, 
            codigoEmpresa
            from Servicios;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar Servicio -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarServicio()
		Begin
        select 
			codigoServicio, 
            fechaServicio,
            tipoServicio, 
            horaServicio, 
            lugarServicio, 
            telefonoContacto, 
            codigoEmpresa
                from Servicios where codigoServicio = codServicio;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Presupuesto ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarPresupuesto(in codigoPresupuesto int,in fechaSolicitud date,in cantidadPresupuesto decimal(10,2),in codigoEmpresa int)
		Begin
			Insert into Presupuestos(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values (fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        End$$
Delimiter ;

call sp_AgregarPresupuesto(1,'2023-05-26',12.44,1);
call sp_AgregarPresupuesto(2,'2023-06-27',12.45,2);
call sp_AgregarPresupuesto(3,'2023-07-28',12.46,3);
call sp_AgregarPresupuesto(4,'2023-08-29',12.47,4);
call sp_AgregarPresupuesto(5,'2023-09-15',12.48,5);
-- --------------------------------------------------- Editar Presupuesto -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarPresupuesto(in fecSolitud date,in canPresupuesto decimal(10,2),in codEmpresa int)
		Begin
        update Presupuestos set fechaSolicitud = fecSolitud, cantidadPresupuesto = canPresupuesto, codigoEmpresa = codEmpresa
				where codigoPresupuesto = codPresupuesto;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Presupuesto ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarPresupuesto(in codEmpresa int)
		Begin
        delete from Presupuestos where  codigoEmpresa = codEmpresa;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Presupuestos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarPresupuestos()
		Begin
			select 
				codigoPresupuesto, 
                fechaSolicitud, 
                cantidadPresupuesto, 
                codigoEmpresa
                from Presupuestos;
            
        End$$
Delimiter ;

call sp_ListarPresupuestos();
-- --------------------------------------------------- Buscar Presupuesto -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarPresupuesto(in codEmpresa int)
		Begin
        select 
				codigoPresupuesto, 
                fechaSolicitud, 
                cantidadPresupuesto, 
                codigoEmpresa
                from Presupuestos where codigoEmpresa = codEmpresa;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Plato ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarPlato(in cantidadPlatos int,in nombrePlato varchar(150),in descripcionPlato varchar(150),in precioPlato decimal(10,2),in codigoTipoPlato int)
		Begin
			Insert into Platos (cantidadPlatos, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values (cantidadPlatos, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End$$
Delimiter ;
call sp_AgregarPlato(1, 'Pizza', 'Pizza con queso', 5.00, 1);
call sp_AgregarPlato(1, 'Papas fritas', 'Gran cantidad Papas', 6.00, 1);
-- --------------------------------------------------- Editar Plato -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarPlatosPlato(in codiPlato int,in cant int,in nombPlato varchar(150),in descriPlato varchar(150),in precPlato decimal(10,2),in codTipoPlato int)
		Begin
        update Platos set cantidadPlatos = cant, nombrePlato = nombPlato , descripcionPlato = descriPlato , precioPlato = precPlato, codigoTipoPlato = codTipoPlato
				where codigoPlato = codiPlato;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Plato ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarPlato(in codTipoPlato int)
		Begin
        delete from Platos where codigoTipoPlato = codTipoPlato;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarPlatos()
		Begin
			select 
				codigoPlato, 
				cantidadPlatos,
				nombrePlato, 
				descripcionPlato, 
				precioPlato, 
				codigoTipoPlato	
            from Platos;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar Plato -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarPlato(in codTipoPlato int)
		Begin
        select 
			codigoPlato, 
            cantidadPlatos,
            nombrePlato, 
            descripcionPlato, 
            precioPlato, 
            codigoTipoPlato	
                from Platos where codigoTipoPlato = codTipoPlato;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Productos_has_Platos ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarProductos_has_Platos(in Productos_codigoProducto int ,in codigoPlato int,in codigoProducto int)
		Begin
			Insert into Productos_has_Platos(Productos_codigoProducto, codigoPlato, codigoProducto)
				values (Productos_codigoProducto, codigoPlato, codigoProducto);
        End$$
Delimiter ;
call sp_AgregarProductos_has_Platos(1,1,1);
call sp_AgregarProductos_has_Platos(2,1,2);
-- --------------------------------------------------- Editar Productos_has_Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarProductos_has_Platos(in productosCodProductos int ,in codPlato int,in codProducto int)
		Begin
        update Productos_has_Platos set codigoPlato = codPlato, codigoProducto = codProducto
				where Productos_codigoProducto = productosCodProductos;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Productos_has_Platos ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarProductos_has_Platos(in proCodigoProducto int)
		Begin
        delete from Productos_has_Platos where Productos_codigoProducto = proCodigoProducto;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Productos_has_Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarProductos_has_Platos()
		Begin
			select 
            Productos_codigoProducto, 
            codigoPlato, 
            codigoProducto
            from Productos_has_Platos;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar Productos_has_Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarProductos_has_Platos(in proCodigoProducto int)
		Begin
        select 
			Productos_codigoProducto, 
            codigoPlato, 
            codigoProducto
            from Productos_has_Platos where Productos_codigoProducto = proCodigoProducto;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Servicios_has_Platos ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarServicios_has_Platos(in Servicios_codigoServicio int,in codigoPlato int,in codigoServicio int)
		Begin
			Insert into Servicios_has_Platos(Servicios_codigoServicio, codigoPlato, codigoServicio)
				values (Servicios_codigoServicio, codigoPlato, codigoServicio);
        End$$
Delimiter ;
call sp_AgregarServicios_has_Platos(1, 1, 1);
call sp_AgregarServicios_has_Platos
(2, 1, 2);
-- --------------------------------------------------- Editar Servicios_has_Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarServicios_has_Platos(in servCodigoServicio int,in codigoPlato int,in codigoServicio int)
		Begin
        update Servicios_has_Platos set codigoPlato = codPlato, codigoServicio = codServicio
				where Servicios_codigoServicio = servCodigoServicio;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Servicios_has_Platos ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarServicios_has_Platos(in SercodigoServicio int)
		Begin
        delete from servicios_has_platos where Servicios_codigoServicio = SercodigoServicio;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Servicios_has_Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarServicios_has_Platos()
		Begin
			select 
				Servicios_codigoServicio, 
                codigoPlato, 
                codigoServicio
                from servicios_has_platos;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar Servicios_has_Platos -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarServicios_has_Platos(in SercodigoServicio int)
		Begin
        select 
				Servicios_codigoServicio, 
                codigoPlato, 
                codigoServicio
                from servicios_has_platos where Servicios_codigoServicio = SercodigoServicio;
        
        End$$
Delimiter ;
--
--
-- --------------------------------------------------- Agregar Servicios_has_Empleado ----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarServicios_has_Empleado(in Servicios_codigoServicio int,in codigoServicio int, in codigoEmpleado int,in fechaEvento date,in horaEvento time,in lugarEvento varchar(150))
		Begin
			Insert into Servicios_has_Empleado(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End$$
Delimiter ;
call sp_AgregarServicios_has_Empleado(1, 1, 1, '2021-08-8', '10:00', 'zona 18');
call sp_AgregarServicios_has_Empleado(2, 1, 1, '2022-05-7', '11:00', 'zona 20');
-- --------------------------------------------------- Editar Servicios_has_Empleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarServicios_has_Empleado(in SercodigoServicio int,in codServicio int, in codEmpleado int,in fecEvento date,in horEvento time,in lugEvento varchar(150))
		Begin
        update Servicios_has_Empleado set codigoServicio = codServicio , codigoEmpleado = codEmpleado, fechaEvento = fecEvento , horaEvento = horEvento , lugarEvento = lugEvento
				where Servicios_codigoServicio = SercodigoServicio;
        End$$
Delimiter ;
-- --------------------------------------------------- Eliminar Servicios_has_Empleado ---------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_EliminarServicios_has_Empleado(in serHasEmpleado int)
		Begin
        delete from Servicios_has_Empleado where  Servicios_codigoServicio = serHasEmpleado;
        End$$
Delimiter ;
-- --------------------------------------------------- Listar Servicios_has_Empleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarServicios_has_Empleado()
		Begin
			select 
				Servicios_codigoServicio, 
                codigoServicio, 
                codigoEmpleado, 
                fechaEvento, 
                horaEvento, 
                lugarEvento
            from	Servicios_has_Empleado;
            
        End$$
Delimiter ;
-- --------------------------------------------------- Buscar Servicios_has_Empleado -----------------------------------------------------------------------------------------------------------------------------
Delimiter $$
	create procedure sp_BuscarServicios_has_Empleado(in serHasEmpleado int)
		Begin
        select 
				Servicios_codigoServicio, 
                codigoServicio, 
                codigoEmpleado, 
                fechaEvento, 
                horaEvento, 
                lugarEvento
                from Servicios_has_Empleado where Servicios_codigoServicio = serHasEmpleado;
        
        End$$
Delimiter ;

-- ------------------------------------------------ Usuario -----------------------------------------------------------------------------------------------------------------------

create table Usuarios(
codigoUsuario int auto_increment not null,
nombreUsuario varchar(25),
apellidoUsuario varchar(25),
usuarioLogin varchar(25),
contrasena varchar(25),
primary key PK_codigousuario(codigousuario) 
);

Delimiter $$
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(25),in apellidoUsuario varchar(25), in usuarioLogin varchar(25), in contrasena varchar(25))
		Begin
			Insert into Usuarios(nombreUsuario, apellidoUsuario, usuarioLogin,contrasena)
				values (nombreUsuario, apellidoUsuario, usuarioLogin,contrasena);
        End$$
Delimiter ;
call sp_AgregarUsuario('Pablo','Bermudez','pbermudez','pbermudez');

select * from Usuarios;

Delimiter $$
	create procedure sp_ListarUsuarios()
		Begin
			select 
				codigoUsuario, 
                nombreUsuario, 
                apellidoUsuario, 
                usuarioLogin, 
                contrasena
            from Usuarios;
            
        End$$
Delimiter ;
call sp_ListarUsuarios();


Delimiter $$
create procedure sp_ServicioEmpresa(in codEmpresa int)
	begin 
		select EM.codigoEmpresa, EM.nombreEmpresa, EM.direccion, EM.telefono, S.codigoServicio, S.tipoServicio, S.lugarServicio, S.horaServicio, SE.fechaEvento, 
			E.nombresEmpleado, E.apellidosEmpleado, TE.descripcion, P.nombrePlato, PRE.cantidadPresupuesto , PR.nombreProducto, PR.cantidad
				from Empresas EM inner join Servicios S on EM.codigoEmpresa = S.codigoEmpresa
					inner join servicios_has_empleado SE on  S.codigoServicio = SE.codigoServicio
					inner join Empleados E on SE.codigoEmpleado = E.codigoEmpleado
					inner join TipoEmpleado TE on E.codigoTipoEmpleado = TE.codigoTipoEmpleado
					inner join servicios_has_platos SP on S.codigoServicio = SP.codigoServicio
					inner join Platos P on SP.codigoPlato = P.codigoPlato
					inner join productos_has_platos PP on P.codigoPlato = PP.codigoPlato
					inner join Productos PR on PP.codigoProducto = PR.codigoProducto
					inner join Presupuestos PRE on EM.codigoEmpresa = PRE.codigoEmpresa
						where EM.codigoEmpresa = codEmpresa;
	
	end $$
Delimiter ;
call sp_ServicioEmpresa(1);