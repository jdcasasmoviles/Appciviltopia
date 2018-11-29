package com.jdcasas.appciviltopia;
public  class BaseDatos {
      //runhosting
       String IP_Server="http://jdcasasciviltopia.atwebpages.com";
    // String IP_Server="http://"+"192.168.2.2";
    String URL="" ;
    public BaseDatos(){
    //  URL = IP_Server+"/AppCiviltopia/serverTabla.php";
     URL = IP_Server+"/serverTabla.php";
            System.out.println("Estoy usando tablas");
    }
    public BaseDatos(int id){
   //  URL = IP_Server+"/AppCiviltopia/acces.php";
    URL = IP_Server+"/acces.php";
        System.out.println("Estoy usando tablas acceso");
    }

    public String accesoLogin(String usuario,String password) {
        usuario=replaceEneEsp(usuario);
        password=replaceEneEsp(password);
        URL = URL+"?operacion=accesoLogin&usuario="+usuario+"&password="+password;
        System.out.println("URL login\n : " + URL);
        return URL;
    }

    public String accesoLoginReceptor(String departamento,String usuario,String password) {
        departamento=departamento.toLowerCase();
        usuario=replaceEneEsp(usuario);
        password=replaceEneEsp(password);
        departamento=replaceEneEsp(departamento);
        URL = URL+"?operacion=accesoLoginReceptor&departamento="+departamento+"&usuario="+usuario+"&password="+password;
        System.out.println("URL login RECEPTOR\n : " + URL);
        return URL;
    }
    public String accesoLoginPublico(String usuario,String password) {
        usuario=replaceEneEsp(usuario);
        password=replaceEneEsp(password);
        URL = URL+"?operacion=accesoLoginPublico&usuario="+usuario+"&password="+password;
        System.out.println("URL login\n : " + URL);
        return URL;
    }
    public String buscaDatosCip(String cip) {
        cip=replaceEneEsp(cip);
        URL = URL+"?operacion=buscaDatosCip&cip="+cip;
        System.out.println("URL DATOS MEDIANTE CIP\n : " + URL);
        return URL;
    }

   /* public String listarProvincias() {
        URL = URL + "?operacion=listaProvincias";
        System.out.println("URL LISTAR PROVINCIAS\n : " + URL);
        return URL;
    }*/

   /* public String listarDepartamentos() {
        URL = URL + "?operacion=listarDepartamentos";
        System.out.println("URL LISTAR DEPARTAMENTOS\n : " + URL);
        return URL;
    }*/

    public String listarDepasProvincias(String departamento) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        URL = URL + "?operacion=listarDepasProvincias&departamento="+departamento;
        System.out.println("URL LISTAR DEPARTAMENTOS  PROVINCIAS\n : " + URL);
        return URL;
    }

  /*  public String listarDistritos(String provincia) {
        provincia=replaceEneEsp(provincia);
        URL = URL + "?operacion=listaDistritos&provincia="+provincia;
        System.out.println("URL LISTAR DISTRITOS\n : " + URL);
        return URL;
    }*/

    public String listaDepasDistritos(String departamento,String provincia) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        provincia=replaceEneEsp(provincia);
        URL = URL + "?operacion=listaDepasDistritos&departamento="+departamento+"&provincia="+provincia;
        System.out.println("URL LISTAR DEPARTAMENTOS SUS DISTRITOS\n : " + URL);
        return URL;
    }


  /*   public String buscarExpedientesProDis(String provincia,String distrito) {
         provincia=replaceEneEsp(provincia);
         distrito=replaceEneEsp(distrito);
            URL = URL + "?operacion=buscarProvinciaDistrito&provincia=" +provincia+"&distrito=" +distrito;
            System.out.println("URL BUSCAR EXPEDIENTES por distrito y provincia\n : " + URL);
        return URL;
    }*/

    public String buscarExpedientesDepasProDis(String departamento, String provincia,String distrito) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        provincia=replaceEneEsp(provincia);
        distrito=replaceEneEsp(distrito);
        URL = URL + "?operacion=buscarExpedientesDepasProDis&departamento="+departamento+"&provincia=" +provincia+"&distrito=" +distrito;
        System.out.println("URL BUSCAR EXPEDIENTES DEPARTAMENTO  en si por distrito y provincia\n : " + URL);
        return URL;
    }



    public String listarUniversidadesIngenieros(String categoria,String especialidad,String estado) {
        estado=replaceEneEsp(estado);
        categoria=replaceEneEsp(categoria);
        especialidad=replaceEneEsp(especialidad);
        URL = URL + "?operacion=listarUniversidadesIngenieros&estado=" +estado+"&categoria=" +categoria+"&especialidad=" +especialidad;
        System.out.println("URL //LISTAR UNIVERSIDADES\n : " + URL);
        return URL;
    }


    public String buscarIngenieros(String estado,String categoria,String especialidad,String universidad) {
        estado=replaceEneEsp(estado);
        categoria=replaceEneEsp(categoria);
        especialidad=replaceEneEsp(especialidad);
        universidad=replaceEneEsp(universidad);
        URL = URL + "?operacion=buscarIngenieros&estado=" +estado+"&categoria=" +categoria+"&especialidad=" +especialidad+"&universidad=" +universidad;
        System.out.println("URL //LISTAR UNIVERSIDADES\n : " + URL);
        return URL;
    }

    public String buscarGeoReceptoresCoordenadas(String departamento,String provincia) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        provincia=replaceEneEsp(provincia);
        URL = URL + "?operacion=buscarGeoReceptoresCoordenadas&departamento="+departamento+"&provincia=" +provincia;
        System.out.println("URL GEOLOCALIZANDO RECEPTORES : " + URL);
        return URL;
    }
    public String buscarGeoIngenierosCoordenadasReceptores(String departamento,String provincia,String distrito) {
        departamento=departamento.toLowerCase();
        departamento=replaceEneEsp(departamento);
        provincia=replaceEneEsp(provincia);
        distrito=replaceEneEsp(distrito);
        URL = URL + "?operacion=buscarGeoIngenierosCoordenadasReceptores&departamento="+departamento+"&provincia=" +provincia+"&distrito=" +distrito;
        System.out.println("URL LISTA RECEPTORES PARA GEO BUSCAR INGENIEROS : " + URL);
        return URL;
    }

    public String buscarGeoIngenierosCoordenadas(String hcentro,String kcentro,String especialidad) {
        hcentro=replaceEneEsp(hcentro);
        kcentro=replaceEneEsp(kcentro);
        especialidad=replaceEneEsp(especialidad);
        URL = URL + "?operacion=buscarGeoIngenierosCoordenadas&hcentro=" +hcentro+"&kcentro=" +kcentro+"&especialidad=" +especialidad;
        System.out.println("URL GEO INGENIEROS  BUSCAR INGENIEROS : " + URL);
        return URL;
    }

    public String nuevaPosicion(String tabla,String usuario,String coorx,String coory) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        usuario=replaceEneEsp(usuario);
        coorx=replaceEneEsp(coorx);
        coory=replaceEneEsp(coory);
        URL = URL + "?operacion=nuevaPosicion&tabla="+tabla+"&usuario=" +usuario+"&coorx=" +coorx+"&coory=" +coory;
        System.out.println("URL CAMBIAR COORDENADAS : " + URL);
        return URL;
    }

   /* public String nuevaPosicionReceptor(String departamento,String usuario,String coorx,String coory) {
        usuario=replaceEneEsp(usuario);
        coorx=replaceEneEsp(coorx);
        coory=replaceEneEsp(coory);
        departamento=replaceEneEsp(departamento);
        URL = URL + "?operacion=nuevaPosicionReceptor&departamento="+departamento+"&usuario=" +usuario+"&coorx=" +coorx+"&coory=" +coory;
        System.out.println("URL CAMBIAR COORDENADAS : " + URL);
        return URL;
    }*/


    public String obtenerPassword(String  tabla ,String usuario) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        usuario=replaceEneEsp(usuario);
        URL = URL + "?operacion=obtenerPassword&tabla=" +tabla+"&usuario=" +usuario;
        System.out.println("URL CAMBIAR PASSWORD  : " + URL);
        return URL;
    }
  /*  public String obtenerPasswordReceptor(String departamento,String usuario) {
        usuario=replaceEneEsp(usuario);
        departamento=replaceEneEsp(departamento);
        URL = URL + "?operacion=obtenerPasswordReceptor&departamento="+departamento+"&usuario=" +usuario;
        System.out.println("URL CAMBIAR PASSWORD RECEPTOR : " + URL);
        return URL;
    }*/

    public String nuevoPassword(String tabla,String usuario,String password) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        usuario=replaceEneEsp(usuario);
        password=replaceEneEsp(password);
        URL = URL + "?operacion=nuevoPassword&tabla="+tabla+"&usuario=" +usuario+"&password=" +password;
        System.out.println("URL CAMBIAR PASS : " + URL);
        return URL;
    }
 /*   public String nuevoPasswordReceptor(String departamento,String usuario,String password) {
        usuario=replaceEneEsp(usuario);
        password=replaceEneEsp(password);
        departamento=replaceEneEsp(departamento);
        URL = URL + "?operacion=nuevoPasswordReceptor&departamento="+departamento+"&usuario=" +usuario+"&password=" +password;
        System.out.println("URL CAMBIAR PASS RECEPTOR : " + URL);
        return URL;
    }*/

    public String nuevolinkcv(String usuario,String linkcv) {
        usuario=replaceEneEsp(usuario);
        linkcv=replaceEneEsp(linkcv);
        URL = URL + "?operacion=nuevolinkcv&usuario=" +usuario+"&linkcv=" +linkcv;
        System.out.println("URL CAMBIAR linkcv : " + URL);
        return URL;
    }


    public String actualizarDatosIngeniero(String usuario,String disponibilidad,String celular,String email) {
        usuario=replaceEneEsp(usuario);
        disponibilidad=replaceEneEsp(disponibilidad);
        celular= replaceEneEsp(celular);
        email= replaceEneEsp(email);
        URL = URL + "?operacion=actualizarDatosIngeniero&usuario=" +usuario+"&disponibilidad=" +disponibilidad+"&celular=" +celular+"&email=" +email;
        System.out.println("URL //ACTUALIZAR DATOS INGENIERO\n : " + URL);
        return URL;

    }

    public String actualizarDatosPublico(String usuario,String disponibilidad,String celular) {
        usuario=replaceEneEsp(usuario);
        disponibilidad=replaceEneEsp(disponibilidad);
        celular= replaceEneEsp(celular);
        URL = URL + "?operacion=actualizarDatosPublico&usuario=" +usuario+"&disponibilidad=" +disponibilidad+"&celular=" +celular;
        System.out.println("URL //ACTUALIZAR DATOS INGENIERO\n : " + URL);
        return URL;

    }

    public String buscarReceptoresCercaTi(String hcentro,String kcentro) {
        hcentro= replaceEneEsp(hcentro);
        kcentro= replaceEneEsp(kcentro);
        URL = URL + "?operacion=buscarReceptoresCercaTi&hcentro=" +hcentro+"&kcentro=" +kcentro;
        System.out.println("URL RECEPTORES CERCA A TI : " + URL);
        return URL;
    }

    /////////////SPINNER //////////
    public String spOpciones(String tabla,String campo) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        campo=replaceEneEsp(campo);
        URL = URL + "?operacion=spOpciones&tabla="+tabla+"&campo="+campo;
        System.out.println("URL SPINNER OPCIONES\n : " + URL);
        return URL;
    }

    /////////////////////INSERTANDO NUEVOS PROYECTOS////////////////////////
    public String insertarProyectos(String tabla,String[] datos) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        URL = URL + "?operacion=insertarProyectos&tabla="+tabla;
        for(int i=0;i<datos.length;i++)
        {
            datos[i]=replaceEneEsp(  datos[i]);
            System.out.println("datos :  "+datos[i] );
            URL = URL +"&datos"+i+"=" +datos[i];;
        }
        System.out.println("URL INSERTAR EXPEDIENTES: " + URL);
        return URL;
    }
    /////////////////////INSERTANDO USUARIOS PUBLICOS////////////////////////
    public String insertarUsuariosPublicos(String tabla,String[] datos) {
        tabla =tabla.toLowerCase();
        tabla=replaceEneEsp(tabla);
        URL = URL + "?operacion=insertarUsuariosPublicos&tabla="+tabla;
        for(int i=0;i<datos.length;i++)
        {
            datos[i]=replaceEneEsp(  datos[i]);
            System.out.println("datos :  "+datos[i] );
            URL = URL +"&datos"+i+"=" +datos[i];;
        }
        System.out.println("URL INSERTAR EXPEDIENTES: " + URL);
        return URL;
    }
    public String replaceEneEsp(String palabra){
        palabra=palabra.replace(" ","%20");
        palabra=palabra.replace("Ñ", "%D1");
        palabra=palabra.replace("ñ", "%F1");
        palabra=palabra.replace("ú", "%FA");
        palabra=palabra.replace("ó", "%F3");
        palabra=palabra.replace("í", "%ED");
        palabra=palabra.replace("é", "%E9");
        palabra=palabra.replace("á", "%E1");
        palabra=palabra.replace("/", "%2F");
        palabra=palabra.replace("@", "%40");
        palabra=palabra.replace("	", "%09");
        palabra=palabra.replace("Á", "%C1");
        palabra=palabra.replace("É", "%C9");
        palabra=palabra.replace("Í", "%CD");
        palabra=palabra.replace("Ó", "%D3");
        palabra=palabra.replace("Ú", "%DA");
        return palabra;
    }

}
