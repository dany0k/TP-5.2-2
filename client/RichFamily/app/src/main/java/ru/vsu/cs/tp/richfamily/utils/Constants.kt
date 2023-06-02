package ru.vsu.cs.tp.richfamily.utils

object Constants {
    const val SUBMIT_WAL_DELETE_TEXT: String = "При удалении счета, также удалятся все расходы и доходы, " +
            "записанные на этот счет.\nВы уверены?"
    const val SUBMIT_CAT_DELETE_TEXT: String = "При удалении категории, также удалятся все расходы и доходы, " +
            "в которых используется данная категория.\nВы уверены?"
    const val CONS_TEXT = "РАСХОД"
    const val INCOME_TEXT = "ДОХОД"
    const val NAME_PLACEHOLDER = "Лучший пользователь"
    const val EMAIL_PLACEHOLDER = "Здесь пока что пусто"
    const val COMP_FIELDS_TOAST = "Заполните все поля!"
    const val SUCCESS = "Успешно!"
    const val DELETE_GROUP_MESSAGE = "При удалении группы все пользваотели автоматически выйдут из нее," +
            " их доходы и расходы будут сохранены\nВы уверены?"
    const val LEAVE_GROUP_MESSAGE = "Вы уверены?"
    const val NO_SUCH_USER = "Ошибка при добавление пользователя"
    const val PWD_NOT_COMPARE = "Пароли не совпадают!"
    const val INVALID_EMAIL = "Неверно заполнено поле email!"
    const val SUCCESS_LOGIN = "Успешно!"
    const val INVALID_DATA = "Неверные данные"
    const val SUCCESS_TOAST = "Успешно!"
    const val PARTICIPANT = "Участник"
    const val LEADER = "Лидер"
    const val SUBMIT_GROUP_USER_DELETE_TEXT = "При удалении пользователя, все расходы и доходы," +
            " созданные им во время участия в группе будут сохранены. " +
            "Вы больше не сможете увидеть операции пользователя.\nПродолжить?"
    const val ONBOARDING_WALLET_TITLE = "Следите за своими финансами"
    const val ONBOARDING_WALLET_DISC = "Создавайте разные счета и категории для точного " +
            "распределения своих финансовых операций"
    const val ONBOARDING_GROUP_TITLE = "Создавайте группы"
    const val ONBOARDING_GROUP_DISC = "Объединяйтесь в группы, чтобы отслеживать финансовые " +
            "операции других участников"
    const val ONBOARDING_ANALYZE_TITLE = "Анализируйте свой бюджет"
    const val ONBOARDING_ANALYZE_DISC = "Вы всегда можете посмотреть на все свои операции," +
            " а также экспортировать их на свое мобильное устройство"
}