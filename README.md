# scala.moscow

[scala.moscow](https://scala.moscow/) website.


## Разворачивание

* установить [Google Cloud SDK](https://cloud.google.com/sdk/docs/)
* установить python sdk `gcloud components install app-engine-python`

### Генерация

через [site-generator](site-generator/)

```
rm -r public/static/*
java -jar scala-moscow-sitegen-assembly.jar gen -o public/static
```

### Тестирование

```
dev_appserver.py ./public
```

### Deploy новой версии

```
gcloud app deploy ./public --project scala-moscow --version x
```

где `x` – версия.
