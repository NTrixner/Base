import {Component, HostBinding, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {OverlayContainer} from '@angular/cdk/overlay';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
})
export class AppComponent implements OnInit {
  title = 'Base Client';

  toggleControl = new FormControl(false);

  @HostBinding('class') className = '';
  darkClassName: string = 'darkMode';
  lightClassName: string = 'lightMode';

  languages = [{lang: 'EN', country: 'GB'}, {lang: 'DE', country: 'AT'}]

  currentLanguage = {lang: 'EN', country: 'GB'};

  constructor(private overlay: OverlayContainer, private translate: TranslateService) {

  }

  public alphaToFlagAlpha = (a: string) => String.fromCodePoint(0x1f1a5 + a.toUpperCase().codePointAt(0)!);

  public emojiFlag = (countryCode: string) => countryCode.slice(0, 2).split("").map(this.alphaToFlagAlpha).join("");

  ngOnInit(): void {
    this.className = localStorage.getItem('theme') ?? this.lightClassName;
    if (this.className == this.darkClassName) {
      this.toggleControl.setValue(true);
    }
    this.updateClasses();
    this.toggleControl.valueChanges.subscribe((darkMode) => {
      this.className = darkMode ? this.darkClassName : this.lightClassName;
      this.updateClasses();
    });
    this.currentLanguage = JSON.parse(localStorage.getItem('language') ?? JSON.stringify(this.currentLanguage)) ?? this.currentLanguage;
    this.updateLanguage();
  }

  private updateClasses() {
    localStorage.setItem('theme', this.className);
    if (!!this.darkClassName) {
      this.overlay.getContainerElement().classList.add(this.darkClassName);
      this.overlay.getContainerElement().classList.remove(this.lightClassName);
    } else {
      this.overlay.getContainerElement().classList.remove(this.darkClassName);
      this.overlay.getContainerElement().classList.add(this.lightClassName);
    }
  }

  changeLanguage(language: { country: string; lang: string }) {
    this.currentLanguage = language;
    this.updateLanguage();
  }

  private updateLanguage() {
    localStorage.setItem('language', JSON.stringify(this.currentLanguage));
    this.translate.setDefaultLang(this.currentLanguage.lang);
    this.translate.use(this.currentLanguage.lang);
  }
}
