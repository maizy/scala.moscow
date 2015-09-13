# scala.moscow

[scala.moscow](http://scala.moscow/) website.


## Обновление

* залить изменения в master текущего репозитория, пометить новым git tag
* при необходимости проверить локально
 * [развернуть deploy виртуалку](https://github.com/scala-moscow/deploy)
 * для проверки статики может быть достаточно в директории `public` запустить
   `python -m SimpleHTTPServer 8000`
* вписать новый тег в `scala_moscow_version` внутри
  [group_vars/all](https://github.com/scala-moscow/deploy/blob/master/group_vars/all)
* в deploy `ansible-play -i production.hosts site.yml`
 * требуется иметь настроенный в .ssh/config хост с названием `sm_root`
   и валидными ssh ключами
