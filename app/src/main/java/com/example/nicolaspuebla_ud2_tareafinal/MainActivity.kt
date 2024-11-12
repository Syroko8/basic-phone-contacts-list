package com.example.nicolaspuebla_ud2_tareafinal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nicolaspuebla_ud2_tareafinal.ui.theme.NicolasPuebla_UD2_TareaFinalTheme

/**
 * @author: Nicolás Puebla 2ºDAMPB.
 *
 * Lo primero que encontramos es la clase MainActivity, la cual establece el tema y  llama
 * a la función composable App().
 * */

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NicolasPuebla_UD2_TareaFinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    App()
                }
            }
        }
    }
}

/**
 * Esta es la función principal de la aplicación.
 * Contiene ciertas variables mutables además del scaffold.
 *
 * En el scaffold encontramos el floatingActionButton. Al pulsarlo cambiamos el valor de la
 * variable showForm, la cual indica si se mostrará el formulario. El valor de esta variable
 * se evalúa en una sentencia if, que usaremos para mostrar u ocultar el formulario.
 * Como la variable es mutable, cada vez que cambie el valor se repintará la función Content.
 *
 * @var actualView = almacena el título que se le mostrará al usuario en el topAppBar, además la usaremos para saber
 * qué usuarios hay que mostrar, dependiendo de si su valor es "Contactos" o "Favoritos".
 * Al ser una variable mutable, hará que se recomponga la lista cuando alternemos entre las dos listas.
 *
 * @var showForm = indica cuándo se debe mostrar el formulario.
 * Al ser mutable ayudará a recomponer el contenido para que se muestre el formulario.
 *
 * Al llamar a la función BottomBar, le pasamos dos funciones lambda que le permiten alternar el valor
 * de la variable actualView.
 *
 * Al llamar a la función Content, le pasamos una función lambda que le permitirá cambiar el
 * valor de la variable showForm a false.
 */


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {

    var actualView by remember {
        mutableStateOf("Contactos")
    }

    var showForm by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopBar(actualView)
        },
        floatingActionButton = {
            // Al pulsarse, mostramos o escondemos el formulario.
            FloatingActionButton(onClick = { showForm = !showForm }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomBar(
                seeContacts = { actualView = "Contactos" },
                seeFavs = { actualView = "Favoritos" }
            )
        }
    ) { innerPadding ->
        Content(
            innerPadding,
            showForm,
            actualView,
            onCreateContact = {showForm = false}
        )
    }
}

/**
 * Esta función contiene el TopAppBar.
 * En este topBar se mostrará el título "Contactos" o "Favoritos", recibido como parámetro.
 *
 * @param actualView = Indica el título que se mostrará en la aplicación.
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(actualView: String){

    TopAppBar(
        // El título lo determinará la variable.
        title = { Text(actualView) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray,
            titleContentColor = Color.White
        )
    )
}

/**
 *  Esta función contiene el BottomAppBar, en el cual encontramos los dos botones usados para alternar entre
 *  las listas de contactos y favoritos.
 *
 *  @param seeContacts = Recibe una función lambda que cambiará el valor de la variable actualView a "Contactos".
 *
 *  @param seeFavs = Recibe una función lambda que cambiará el valor de la variable actualView a "Favoritos".
 *
 * */
@Composable
fun BottomBar(seeContacts: () -> Unit, seeFavs: () -> Unit) {

    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                // Al pulsarse, asigna a la variable actualView el valor "Contactos".
                Button(onClick = { seeContacts() }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Contactos")
                }
                Text("Contactos")
            }
            Column {
                // Al pulsarse, asigna a la variable actualView el valor "Favoritos".
                Button(onClick = { seeFavs() }) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favoritos")
                }
                Text("Favoritos")
            }
        }
    }

}

/**
 * Esta función contiene el contenido de la aplicación.
 *
 * El estado de los contactos se organiza empleando el atributo state de la clase Contact.
 * Cada vez que se quiere actualizar el valor de este atributo, para que la lista detecte que
 * hay un cambio en su estructura, se sobrescribe el objeto por una copia de sí mismo, pero con el
 * valor del atributo state siendo el contrario, ya que tiene un valor booleano. Al hacer esto conseguimos
 * que se repinten las funciones en las que empleamos la lista.
 *
 * @param showForm = Recibe el valor de la variable booleana  showForm, que indica el estado de
 * visibilidad del formulario.
 *
 * @param actualView = Recibe el valor de la variable actualView, que indica cuál de las dos listas debe mostrarse.
 *
 * @param onCreateContact = Se ejecuta al pulsarse el botón del formulario para agregar el nuevo usuario.
 * Cambia el valor de la variable showForm, haciendo que el formulario se oculte.
 *
 * @var contactList = almacena una lista de objetos de la clase Contact, la cual contiene al información
 * de cada contacto.
 *
 * @fun introduceContact = Función que recibe un nuevo contacto y lo introduce en la lista.
 *
 * @fun modifyContact = Recibe un contacto, obtiene su índice en la lista, y sobrescribe el objeto,
 * pero con el atributo state cambiado.
 *
 * Al llamar a la función Formulario, le pasamos una función lambda que obtiene el objeto que mandará
 * esta función y se lo pasamos a la función introduceContact, que lo introducirá en la lista.
 * Lo mismo hacemos al llamar a la función ContactList, que introducirá el objeto recibido en la
 * función modifyContact.
 *
 * */

@Composable
fun Content(innerPadding: PaddingValues, showForm: Boolean, actualView: String, onCreateContact: () -> Unit) {

    var contactList = remember { mutableStateListOf<Contact>() }

    fun introduceContact(contact: Contact){
        contactList.add(contact)
    }

    fun modifyContact(contact: Contact){
        var index = contactList.indexOf(contact)
        contactList[index] = contactList[index].copy(state = !contact.state)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(15.dp)
    ) {
        // Dependiendo del valor de la variable, mostraremos el formulario.
        if (showForm) {
            Formulario(changeShow = onCreateContact, onConfirm = {introduceContact(it)}, contactList)
        }

        Spacer(Modifier.height(20.dp))
        Row (
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ){
            ContactList(contactList, actualView, onClick = { modifyContact(it) })
        }

    }
}


/**
 * Esta función contiene los elementos del formulario.
 *
 * Cuando se pulsa el botón para añadir el contacto, se evalúa el valor introduciendo en los campos.
 * Después se crea un objeto Contact, y se comprueba que no exista ya en la lista.
 * Si los valores son correctos y el objeto no existe ya en la lista, mandamos el objeto a la función
 * Content a través de los atributos. En Content se añade el objeto a la lista.
 *
 * @param changeShow = Llama al parámetro onCreateContact de la función Content.
 *
 * @param onConfirm = Recibe un contacto y se lo pasa a la función introduce contact.
 *
 * @var name = Contiene el valor escrito en el campo del formulario para el nombre del nuevo contacto.
 *
 * @var number = Contiene el valor escrito en el campo del formulario para el teléfono del nuevo contacto.
 *
 * @var alert = Contiene el mensaje de alerta que se le mostrará al usuario en caso de introducir un valor
 * no numérico en el campo para el teléfono del nuevo contacto, o en caso de que el número de dígitos no sea 9.
 *
 * @fun isNumeric = Comprueba que el valor introducido en el campo para el número del contacto sea numérico.
 * Recibe el número de teléfono del contacto a añadir y devuelve un valor booleano dependiendo de si el valor era numérico.
 *
 * @fun introduceContact = Antes de pasarle el contacto al parámetro onConfirm, comprobamos que el objeto no exista ya en la
 * lista, si no, alertamos. Si no existe le pasa el objeto Contacto al parámetro, y devuelve un false para que no
 * desaparezca el formulario.
 * */
@Composable
fun Formulario(changeShow: () -> Unit, onConfirm: (Contact) -> Unit, list: List<Contact>){

    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var alert by remember { mutableStateOf("") }

    //Se ejecuta al pulsarse el boton del formulario para agregar el nuevo usuario.
    fun introduceContact(): Boolean{
        var newContact = Contact(name, number)
        // Comprobamos que no exista otro contacto igual en la lista.
        if(list.indexOf(newContact) != -1){
            alert = "Ya existe un contacto con estos datos."
            // Interrumpimos el método.
            return false
        }

        onConfirm(newContact)
        // Vaciamos los campos.
        name = ""
        number = ""

        return true
    }

    fun isNumeric(num: String): Boolean{
        var numerico = true
        // Si alguno de los caracteres no es un dígito, no consideramos la cadena como un valor numérico.
        num.forEach{it -> if(!it.isDigit()) numerico = false}
        return numerico
    }

    Row() {
        Column {
            TextField(
                placeholder = { Text("Nombre") },
                onValueChange = { name = it },
                value = name
            )
            TextField(
                placeholder = { Text("Telefono") },
                onValueChange = { number = it },
                value = number
            )

            Spacer(Modifier.height(20.dp))

            Button(onClick = {

                if((isNumeric(number) == true)){
                    if ((name != "") && (number != "") && (number.length == 9)) {
                        alert = ""
                        // Si se cumplen todos los requisitos, ocultamos el formulario y guardamos el contacto.
                        if(introduceContact()){
                            changeShow()
                        }
                    } else{
                        alert = "El teléfono debe de tener 9 dígitos"
                    }
                } else{
                    alert = "El teléfono solo puede contener valores numéricos."
                }
            }) {
                Text("Añadir contacto")
            }

            if(alert != ""){
                Spacer(Modifier.height(15.dp))
            }
            Text(alert)
        }
    }
}

/**
 * Esta función contiene el LazyColum que conforma el listado de contactos.
 *
 * Para alternar entre la lista de contactos y de favoritos, utilizamos el valor de la variable
 * actualView. El valor de esta variable es alterado por los botones del BottomAppBar, cambiando el
 * valor a "Contactos" o a "Favoritos", dependiendo del botón pulsado. Usando una sentencia if,
 * decidimos si iteramos para crear los Cards sobre la lista de contactos o sobre la lista de
 * contactos filtrada para usar solo aquellos contactos cuyo atributo state posea un valor positivo.
 * Como esta variable es mutable, se repintará la lista cada vez que cambie su valor.
 *
 * @param list = Lista de contactos a mostrar.
 *
 * @param actualView = Indica lista que se debe mostrar.
 *
 * @param onClick = Llama a la función modifyContact de la función Content, y le pasa el
 * objeto a modificar como parámetro.
 *
 * */
@Composable
fun ContactList(list: List<Contact>, actualView: String, onClick: (Contact) -> Unit){

    LazyColumn(
        Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(
            10.dp,
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.Start
    ) {
        items(if(actualView == "Contactos") list else list.filter { it.state }) { item ->
            Card(itemShowed = item, onClick = onClick)
        }

    }

}

/**
 * Esta función contiene el Card en el que se mostrará la información de cada contacto.
 *
 * Esta función incluye el Switch con el que podemos añadir un contacto a favoritos. Al pulsarlo,
 * se envía el objeto Contact seleccionado a través de los parámetro de las funciones hasta la función Content,
 * donde se modificará la lista.
 *
 * @param itemShowed = Recibe el contacto que se mostrará.
 *
 * @param onClick = LLama al parámetro onClick de la función ContactList y le pasa el objeto
 * a modificar como parámetro.
 * */

@Composable
fun Card(itemShowed: Contact, onClick: (Contact) -> Unit){

    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Icono de perfil"
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight().width(200.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(itemShowed.name)
                Text(itemShowed.number)
            }

            Column(
                modifier = Modifier.fillMaxHeight().padding(end = 15.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Switch(
                    checked = itemShowed.state,
                    onCheckedChange = {
                        onClick(itemShowed)
                    }
                )
            }
        }
    }

}

/**
 * A partir de esta clase se crearán los contactos.
 *
 * @param newNombre = El nombre del contacto creado.
 *
 * @param newTelefono = El teléfono del contacto creado.
 *
 * @param state = Indica si el usuario está en la lista de favoritos.
 *
 * */
data class Contact(var name: String, var number: String, var state: Boolean = false)

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    NicolasPuebla_UD2_TareaFinalTheme {
        App()
    }
}