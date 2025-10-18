App-Design 

Überblick
    Das MRP-Backend ist eine kleine, übersichtlich strukturierte Java-Anwendung, die auf dem integrierten com.sun.net.httpserver.HttpServer basiert.
    Eingehende HTTP-Anfragen laufen vom Server über den Handler zur MrpApplication, wo ein einfacher Router sie an die passenden Controller weiterleitet – entweder den AuthController oder den MediaController.
    Die Controller sind bewusst schlank gehalten: Sie nehmen JSON-Anfragen entgegen, verarbeiten sie mit einem gemeinsamen ObjectMapper und geben ebenfalls JSON-Antworten zurück.
    Die eigentliche Geschäftslogik liegt in den Services (AuthService, MediaService), die wiederum über Repositories (UserRepository, MediaRepository) direkt mit der PostgreSQL-Datenbank kommunizieren.    
    Die Authentifizierung funktioniert komplett zustandslos. Der TokenService erstellt und überprüft Tokens, die mit einem geheimen Schlüssel signiert sind und im Header Authorization: Bearer … mitgesendet werden.

Zentrale Designentscheidungen
    Klare Schichtenstruktur (Controller → Service → Repository): sorgt für sauberen Code, klare Zuständigkeiten und einfaches Testen.
    Zustandslose Authentifizierung: mithilfe des TokenService – kein Sitzungs- oder Serverzustand nötig.    
    Eigener, einfacher Router.   
    Klare Trennung von JSON und Datenmodellen: JSON-Verarbeitung nur in den Controllern, DTOs für Eingaben, Datenmodelle für die Datenbank.    
    Sicherheitslogik im Service: nur der Ersteller eines Mediums darf es verändern oder löschen.
