from .models import OnboardScreen


def get_screens_by_type(onboard_type: str):
    """ Выбрать из базы данных экраны с определенным типом onboard_type """
    onboards = OnboardScreen.objects.filter(onboard_type=onboard_type).order_by('id')
    return onboards
