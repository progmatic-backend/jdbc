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
Mivel a `Connection` implementálja az `Autocloseable` interfészt, ezért használhatjuk try-with-resources-zal:
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
PreparedStatement preparedStatement = connection.prepareStatement( "SELECT i.*, j.* FROM Omega i, Zappa j WHERE i = ? AND j = ?" );
try {
    preparedStatement.setString(1, "Zámbó Jimmy");
    preparedStatement.setInt(2, 666);
    ResultSet resultSet = preparedStatement.executeQuery();
    try {
        while (resultSet.next()) {
	    // Sorfeldolgozás.
        }
    } finally {
        resultSet.close();
   }
} finally {
   preparedStatement.close();
}
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

## 1. Összes pizza összes adata
Futtasd le és értelmzed a már meglévő kódot, ami kilistázza az összes pizza összes adatát!

### 1.a Összes pizza azonosítója és neve
Módosítsd a programod, hogy a pizzáknak ne írja ki minden adatát, csak az azonosítójukat és nevüket!
Ne legyenek nem használt változók a kódodban!

### 1.b Összes pizza neve
Írd át úgy a kódot, hogy a pizzáknak csak a nevei legyenek kiírva és ügyelj arra, hogy továbbra se maradjanak
felesleges sorok a programodban!

## 2. Olcsó pizzák
Listázd ki a max. 1000 Ft-ba kerülő pizzák adatait!

## 3. Átlagos pizzaár
Írd ki, mennyibe kerül átlagosan egy pizza!

## 4. Burzsuj pizzák
Írd ki, hány darab átlagár feletti pizza van!
