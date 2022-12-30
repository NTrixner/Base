import {MatPaginatorIntl} from '@angular/material/paginator';
import {Injectable, OnDestroy} from '@angular/core';
import {Subject} from 'rxjs';
import {TranslateService} from '@ngx-translate/core';

@Injectable()
export class TranslatedMatPaginatorIntl extends MatPaginatorIntl
  implements OnDestroy {
  unsubscribe: Subject<void> = new Subject<void>();
  OF_LABEL = 'of';

  constructor(private translate: TranslateService) {
    super();

    this.translate.onLangChange
      .subscribe(() => {
        this.getAndInitTranslations();
      });

    this.getAndInitTranslations();
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  getAndInitTranslations() {
    this.translate
      .get([
        'paginator.ipp',
        'paginator.next',
        'paginator.previous',
        'paginator.of',
      ])
      .subscribe(translation => {
        this.itemsPerPageLabel =
          translation['paginator.ipp'];
        this.nextPageLabel = translation['paginator.next'];
        this.previousPageLabel =
          translation['paginator.previous'];
        this.OF_LABEL = translation['paginator.of'];
        this.firstPageLabel = translation['paginator.first'];
        this.lastPageLabel = translation['paginator.last'];
        this.changes.next();
      });
  }

  getRangeLabel = (
    page: number,
    pageSize: number,
    length: number,
  ) => {
    if (length === 0 || pageSize === 0) {
      return `0 ${this.OF_LABEL} ${length}`;
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex =
      startIndex < length
        ? Math.min(startIndex + pageSize, length)
        : startIndex + pageSize;
    return `${startIndex + 1} - ${endIndex} ${
      this.OF_LABEL
    } ${length}`;
  };
}
