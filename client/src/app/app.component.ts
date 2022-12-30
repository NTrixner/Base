import {Component, HostBinding, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {OverlayContainer} from '@angular/cdk/overlay';

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

  constructor(private overlay: OverlayContainer) {
  }

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
}
