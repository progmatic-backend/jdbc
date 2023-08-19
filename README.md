# JDBC

A JDBC a **J**ava **D**ata**b**ase **C**onnectivity-t rövidíti és Java kódból történő 
adatbázis elérést és manipulálást tesz lehetővé.

A JDBC bármilyen adatbázissal képes működni, amennyiben rendelkezésre állnak megfelelő driver-ek ("illesztőprogramok").

# Kód Walkthrough
## JDBC hozzáadása a projekthez
File &rarr; Project Structure &rarr; Libraries &rarr; a felső + jel &rarr; From Maven...

Írd be, hogy `mysql`, nyomj egy entert (és várj, amíg betöltődik) és válaszd ki az egyik mysql-connectort!
(Pl. `mysql:mysql-connector-java:8.0.30`)

## Driver betöltése
A használt adatbázisszerver JDBC driver-ét a `java.lang.Class.forName(String)` metódussal lehet betölteni. 
Az alábbi programsor egy valamilyen JDBC forgalmazó driverét tölti be az alkalmazásba. 
(Egyes virtuális gépek megkövetelik a `Driver` objektum példányosítását is a `.newInstance()` metódussal.)

```
Class.forName( "com.valamijdbcforgalmazo.ValamiJdbcDriver" );
```

:sunglasses: A JDBC 4.0 verziótól már nem kell explicit betölteni a JDBC drivert a `Class.forName()` metódussal.

Miután a virtuális gép betöltötte a `java.sql.Driver` osztályt, példányosítja azt és regisztrálja a 
`java.sql.DriverManager.registerDriver(Driver)` metódussal. 
Ezt a példányosító és regisztráló kódot a driver gyártójának kell implementálnia a driver osztály statikus konstruktorában.

## Adatbázis kapcsolat létrehozása
A következő lépés az adatbáziskapcsolat létrehozása egy `Connection` példány formájában a 
`DriverManager.getConnection()` metódus segítségével:

```
Connection connection = DriverManager.getConnection(
     "jdbc:valamijdbcforgalmazo:további adatok a jdbc forgalmazótól függően",
     "felhasznalonev",
     "jelszo" );
```
A mostani példában a hibakezelés egy nagy try-cath blokkba lett összevonva - ez nem szép, de most a 
legegyszerűbb megoldásra törekedtünk.

A későbbiekben mivel a `Connection` implementálja az `Autocloseable` interfészt,
ezért akár használhatjuk try-with-resources-zal:
```
try (Connection connection = DriverManager
.getConnection("jdbc:mysql://localhost:3306/adatbazis_neve", "felhasznalonev", "jelszo")) {
    // Connection használata a blokkon belül...
}
```

Az URL mindig a „jdbc:” karaktersorozattal kezdődik, a többi része a forgalmazótól függ. 

## SQL parancsok
Ha megvan az adatbáziskapcsolat, akkor létre lehet hozni az SQL kifejezést:

```
Statement statement = connection.createStatement();
try {
    statement.executeUpdate( "INSERT INTO my_table(name) VALUES ('Andris') " );
} finally {
    // Fontos lezárni a kifejezést!
    statement.close();
}
```

A JDBC objektumok lezárása nagyon fontos, mert az adatbáziskapcsolatok, kifejezések és eredményhalmazok erőforrásokat,
például socket-eket és file descriptorokat, foglalnak le az operációs rendszerben. 
Távoli szerver esetében a szerveren kurzorokat is lefoglalhatnak. 
A nyitva felejtett objektumok váratlan és zavarbaejtő hibákhoz vezethetnek. 

A JDBC objektumok használatakor ajánlatos követni az alábbi try-finally mintát:
```
Statement statement = connection.createStatement();
try {
    ResultSet resultSet = statement.executeQuery( "SELECT * FROM my_table" );
    try {
        while (resultSet.next()) {
	        // Feldolgozzuk a sorokat...
        }
    } finally {
        resultSet.close();
    }
} finally {
    statement.close();
}
```

A `PreparedStatement` használata hasonlít a `Statement` használatához, de dinamikusan paraméterezhető:

```
 String query = "INSERT INTO persons(name, birthYear) VALUES( ?, ?)";

 PreparedStatement preparedStatement = connection.prepareStatement(query);
 
 preparedStatement.setString(1, "Dzsámbó Zimmy");
 preparedStatement.setInt(2, 1958);
 
 preparedStatement.executeUpdate();
```

Az alábbi SQL típusokra így lehet konvertálni változókat a Java nyelvből:

| SQL típus |      set()      |  
|:---------:|:---------------:|
|   CHAR    |   setString()   |  
|  VARCHAR  |   setString()   |  
|  NUMBER   | setBigDecimal() |  
|           |  setBoolean()   |  
|           |    setByte()    |  
|           |   setShort()    |  
|           |    setInt()     |  
|           |    setLong()    |  
|           |   setFloat()    |  
|           |   setDouble()   |  
|  INTEGER  |    setInt()     |  
|   FLOAT   |   setDouble()   |   
|   BLOB    |    setBlob()    |
|   DATE    |    setDate()    |  
|           |    setTime()    |  
|           | setTimestamp()  |  

## Lekérdezések

### 0.) Összes pizza összes adata
Futtasd le és értelmzed a már meglévő kódot, ami kilistázza az összes pizza összes adatát!

### 1.) Azonosítók és nevek
Módosítsd a programod, hogy a pizzáknak ne írja ki minden adatát, csak az azonosítójukat és nevüket!
Ne legyenek nem használt változók a kódodban!

### 2.) Csak a nevek
Írd át úgy a kódot, hogy a pizzáknak csak a nevei legyenek kiírva és ügyelj arra, hogy továbbra se maradjanak
felesleges sorok a programodban!

### 3.) Olcsó pizzák
Listázd ki a max. 1000 Ft-ba kerülő pizzák adatait! Az ár szerinti szűrést mely helyeken tudnád megtenni?
Csináld meg mindkétféleképpen!

### 4.) Átlagos pizzaár
Írd ki, mennyibe kerül átlagosan egy pizza!

### 5.) Burzsuj pizzák
Írd ki, hány darab átlagár feletti pizza van!

## Hibák
Próbáld ki mi történik, ha
- rossz jelszót adsz meg a Connectionnek
- nem létező adatbázishoz akarsz csatlakozni
- elgépeled a lekérdezést (pl. `SELECT` helyett azt irod, hogy `SELECC`)
- nem létező táblát adsz meg a lekérdezésben
- nem létező oszlopot adsz meg a lekérdezésben vagy a ResultSet parse-olásakor


## Példányosítás
Készítsünk az adatbázisunkban szereplő pizza tábla soraiból Pizza objektumokat!
A táblában 5 sor van, tehát 5 darab pizzát fogunk példányosítani. 
Ehhez Java kódban is meg kell írnunk egy Pizza osztályt, ami tartalmazza a megfelelő attribútumokat (id, név, ár).

**Tipp**: használj record-ot sima class helyett!

További lépések:
- készíts egy pizzákat tartalmazó listát (`pizzaList`), inicializáld egy üres ArrayList-tel
- az SQL lekérdezésedet írd át arra, hogy az összes pizza összes adatát kérdezze le
- az eredményhalmazon való végigiteráláskor hívd meg a Pizza osztály konstruktorát a megfelelő adatokkal és
add ezt hozzá a `pizzaList`-hez!

Írasd ki a `pizzaList`-et!

Ilyesmit kéne látnod:
`[Pizza[id=1, name=Capricciosa, price=1050], Pizza[id=2, name=Frutti di Mare, price=1350], Pizza[id=3, name=Hawaii, price=850], Pizza[id=4, name=Vesuvio, price=900], Pizza[id=5, name=Sorrento, price=1050]]
`

## Extrák
A leírásban szerepelt, hogy JDBC 4.0-tól nincs szükség a `Class.forName()` metódussal betölteni a drivert,
próbáld ki, hogy ezt a sort kikommenteled! Mi történik, miért?
