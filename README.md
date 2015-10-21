# scala.moscow

[![Join the chat at https://gitter.im/scala-moscow/scala.moscow](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scala-moscow/scala.moscow?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[scala.moscow](http://scala.moscow/) website.


## Обновление

* залить изменения в master текущего репозитория, пометить новым git tag
 * генерация через [site-generator](site-generator/)
* при необходимости проверить локально
 * [развернуть deploy виртуалку](https://github.com/scala-moscow/deploy)
 * для проверки статики может быть достаточно в директории `public` запустить
   `python -m SimpleHTTPServer 8000`
* вписать новый тег в `scala_moscow_version` внутри
  [group_vars/all](https://github.com/scala-moscow/deploy/blob/master/group_vars/all)
* в deploy `ansible-playbook -i production.hosts site.yml`
 * требуется иметь настроенный в .ssh/config хост с названием `sm_root`
   и валидными ssh ключами
