<!DOCTYPE html>
<html>
<body style="text-align: center;">
<h1>Hallo ${username}!
</h1>
<p>Du hast einen Passwort-Reset bei ${applicationName} beantragt. Bitte folge diesem Link, um das Passwort zurück zu
    setzen:</p>
<p><a href="${url}?uuid=${token}">Klick mich!</a></p>
<p>Falls dieser Link nicht funktioniert, kopiere folgenden Text in die Suchleiste deines Internet-Browsers:</p>
<p>${url}?uuid=${token}</p>
<p>Falls du nicht bei ${applicationName} registriert bist oder keinen Antrag auf Passwort-Reset gestellt hast, kannst du
    diese Mail ignorieren oder einen Administrator kontaktieren.</p>
</body>
</html>